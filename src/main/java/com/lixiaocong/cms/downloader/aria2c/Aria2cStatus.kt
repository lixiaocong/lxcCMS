package com.lixiaocong.cms.downloader.aria2c

data class Aria2cStatus(
        val id:String,
        val jsonrpc:String,
        val result:Aria2cStatusResult
)