package com.lyy.retroapollo

import com.apollographql.apollo.api.Query
import com.lyy.retroapollo.annotation.GraphQLQuery
import com.lyy.retroapollo.utils.Utils
import com.lyy.retroapollo.utils.error
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 *
 * buildBuilderMethod 指方法名为build并用来创建Builder的方法
 * buildQueryMethod 指Builder类中的build()方法，该方法用来创建Query
 * fieldSetters 指Builder类中定义的方法的集合列表
 */
class ApolloServiceMethod<T : Any>(
    private val retroApollo: RetroApollo,
    val method: Method,
    private val buildBuilderMethod: Method,
    private val buildQueryMethod: Method,
    private val fieldSetters: List<Method>,
    private val callAdapter: CallAdapter<Any, T>
) {

    class Builder(private val retroApollo: RetroApollo, val method: Method) {

        private val buildBuilderMethod: Method
        private val buildQueryMethod: Method
        private val fieldSetters = ArrayList<Method>()
        private val callAdapter: CallAdapter<Any, Any>

        init {
            val returnType = method.genericReturnType

            //判断是否有泛型的类型
            if (Utils.hasUnresolvableType(returnType)) {
                throw method.error(
                    "Method return type must not include a type variable or wildcard: %s",
                    returnType
                )
            }

            //判断是否返回值为Void
            if (returnType === Void.TYPE) {
                throw method.error("Service methods cannot return void.")
            }
            if (returnType !is ParameterizedType) {
                val name = (returnType as Class<*>).simpleName
                throw IllegalStateException("$name return type must be parameterized as $name<Foo> or $name<out Foo>")
            }

            callAdapter = retroApollo.getCallAdapter(returnType)
                ?: throw  IllegalStateException("$returnType is not supported.")


            /**
             * Observable<RepositoryIssueCountQuery.Data> 为 returnType
             * RepositoryIssueCountQuery.Data 为 callAdapter.responseType()
             * RepositoryIssueCountQuery.Data.class 为 dataType
             */
            val dataType = callAdapter.responseType() as Class<*>

            /**
             * dataType的外部类的getDeclaredMethod
             */
            buildBuilderMethod = dataType.enclosingClass.getDeclaredMethod("builder")

            /**
             * dataType的外部类的内部类
             */
            val builderClass =
                dataType.enclosingClass.declaredClasses.first { it.simpleName == "Builder" }

            /**
             * 方法里面参数的注解
             * first  指Annotation注解的数组
             * second 指参数的类型
             */
            method.parameterAnnotations.zip(method.parameterTypes)
                .mapTo(fieldSetters) { (first, second) ->
                    val annotation = first.first { it is GraphQLQuery } as GraphQLQuery

                    //内部类Builder类里面的方法，方法名为注解里的值，方法的参数类型
                    builderClass.getDeclaredMethod(annotation.value, second)
                }

            buildQueryMethod = builderClass.getDeclaredMethod("build")
        }

        fun build() = ApolloServiceMethod(
            retroApollo,
            method,
            buildBuilderMethod,
            buildQueryMethod,
            fieldSetters,
            callAdapter
        )

    }

    operator fun invoke(args: Array<Any>?): T {
        val builder = buildBuilderMethod(null)
        args?.let {
            fieldSetters.zip(it).forEach {
                it.first.invoke(builder, it.second)
            }
        }

        //RepositoryIssueCountQuery.builder().owner(xxx).repo(xxx).build()
        return callAdapter.adapt(retroApollo.apolloClient.query(buildQueryMethod(builder) as Query<*, Any, *>))
    }

}