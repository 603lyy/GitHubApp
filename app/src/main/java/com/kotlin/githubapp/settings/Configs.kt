package com.kotlin.githubapp.settings

import com.kotlin.common.log.logger
import com.kotlin.githubapp.AppContext
import com.kotlin.githubapp.utils.deviceId

object Configs {

    object Account{
        val SCOPES = listOf("user", "repo", "notifications", "gist", "admin:org")
        const val clientId = "a5063ab3868f6218ac8b"
        const val clientSecret = "4874602afa6106f06d8d86ac70d000f1c0fc1d23"
        const val note = "kotliner.cn"
        const val noteUrl = "http://www.kotliner.cn"
        const val accessToken = "eb9fc35224ac0cf59cbd55e8e482756bc7597f8f "

        val fingerPrint by lazy {
            (AppContext.deviceId + clientId).also { logger.info("fingerPrint: "+it) }
        }
    }

}