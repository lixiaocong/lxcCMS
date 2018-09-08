/*
  BSD 3-Clause License

  Copyright (c) 2016, lixiaocong(lxccs@iCloud.com)
  All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are met:

  * Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.

  * Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

  * Neither the name of the copyright holder nor the names of its
    contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.lixiaocong.cms.downloader

import com.lixiaocong.cms.service.IConfigService
import com.lixiaocong.downloader.DownloadTask
import com.lixiaocong.downloader.DownloaderException
import com.lixiaocong.downloader.IDownloader
import com.lixiaocong.downloader.aria2c4j.AriaClient
import com.lixiaocong.downloader.transmission4j.TransmissionClient
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.util.*

class UnionDownloader(private var configService: IConfigService) : IDownloader {

    private val log: Log = LogFactory.getLog(javaClass)
    private val downloaderMap: MutableMap<String, IDownloader>

    private var aria2cClient: IDownloader
    private var aria2cUrl: String
    private var aria2cPassword: String
    private var aria2cDir: String

    private var transmissionClient: IDownloader
    private var transmissionUrl: String
    private var transmissionUsername: String
    private var transmissionPassword: String

    init {
        this.downloaderMap = HashMap()

        this.aria2cUrl = configService.downloaderAria2cUrl
        this.aria2cPassword = configService.downloaderAria2cPassword
        this.aria2cDir = configService.storageDir
        this.aria2cClient = AriaClient(this.aria2cUrl, this.aria2cPassword, this.aria2cDir)

        this.transmissionUrl = configService.downloaderTransmissionUrl
        this.transmissionUsername = configService.downloaderTransmissionUsername
        this.transmissionPassword = configService.downloaderTransmissionPassword
        this.transmissionClient = TransmissionClient(this.transmissionUsername, this.transmissionPassword, this.transmissionUrl)
    }

    fun checkAria() {
        val newAria2cUrl = this.configService.downloaderAria2cUrl
        val newAria2cPassword = this.configService.downloaderAria2cPassword
        val newAria2cDir = this.configService.storageDir
        if (newAria2cUrl != this.aria2cUrl || newAria2cPassword != this.aria2cPassword || newAria2cDir != this.aria2cDir) {
            log.info("update Aria2c")
            this.aria2cClient = AriaClient(newAria2cUrl, newAria2cPassword, newAria2cDir)
            this.aria2cUrl = newAria2cUrl
            this.aria2cPassword = newAria2cPassword
            this.aria2cDir = newAria2cDir
        }
    }

    fun checkTransmission() {
        val newTransmissionUrl = this.configService.downloaderTransmissionUrl
        val newTransmissionUsername = this.configService.downloaderTransmissionUsername
        val newTransmissionPassword = this.configService.downloaderTransmissionPassword
        if (newTransmissionUrl != this.transmissionUrl || newTransmissionUsername != this.transmissionUsername || newTransmissionPassword != this.transmissionPassword) {
            log.info("update Transmission")
            this.transmissionClient = TransmissionClient(newTransmissionUsername, newTransmissionPassword, newTransmissionUrl)
            this.transmissionUrl = newTransmissionUrl
            this.transmissionUsername = newTransmissionUsername
            this.transmissionPassword = newTransmissionPassword
        }
    }

    @Throws(DownloaderException::class)
    override fun addByMetainfo(metainfo: String) {
        return this.transmissionClient.addByMetainfo(metainfo)
    }

    @Throws(DownloaderException::class)
    override fun addByMetalink(metalink: String) {
        this.transmissionClient.addByMetalink(metalink)
    }

    @Throws(DownloaderException::class)
    override fun addByUrl(url: String) {
        aria2cClient.addByUrl(url)
    }

    @Throws(DownloaderException::class)
    override fun remove(ids: List<String>) {
        val (aria2cList, transmissionList) = divideList(ids)
        aria2cClient.remove(aria2cList)
        transmissionClient.remove(transmissionList)
        ids.forEach { id ->
            downloaderMap.remove(id)
        }
    }

    @Throws(DownloaderException::class)
    override fun remove(id: String) {
        downloaderMap[id]!!.remove(id)
        downloaderMap.remove(id)
    }

    @Throws(DownloaderException::class)
    override fun remove() {
        transmissionClient.remove()
        aria2cClient.remove()
        downloaderMap.clear()
    }

    @Throws(DownloaderException::class)
    override fun start(ids: List<String>) {
        val (aria2cList, transmissionList) = divideList(ids)
        transmissionClient.start(transmissionList)
        aria2cClient.start(aria2cList)
    }

    @Throws(DownloaderException::class)
    override fun start(id: String) {
        downloaderMap[id]!!.start(id)
    }

    @Throws(DownloaderException::class)
    override fun start() {
        transmissionClient.start()
        aria2cClient.start()
    }

    @Throws(DownloaderException::class)
    override fun stop(ids: List<String>) {
        val (aria2cList, transmissionList) = divideList(ids)
        aria2cClient.stop(aria2cList)
        transmissionClient.stop(transmissionList)
    }

    @Throws(DownloaderException::class)
    override fun stop(id: String) {
        downloaderMap[id]!!.stop(id)
    }

    @Throws(DownloaderException::class)
    override fun stop() {
        aria2cClient.stop()
        transmissionClient.stop()
    }

    @Throws(DownloaderException::class)
    override fun get(gid: String): DownloadTask? {
        return downloaderMap[gid]?.get(gid)
    }

    @Throws(DownloaderException::class)
    override fun get(): List<DownloadTask>? {
        val ret = LinkedList<DownloadTask>()

        val aria2cList = aria2cClient.get()
        aria2cList.forEach { task ->
            downloaderMap.put(task.id, aria2cClient)
        }

        val transmissionList = transmissionClient.get()
        transmissionList.forEach { task ->
            downloaderMap.put(task.id, transmissionClient)
        }

        ret.addAll(aria2cList)
        ret.addAll(transmissionList)
        return ret
    }

    data class DivideResult(var tList: List<String>, var aList: List<String>)

    private fun divideList(ids: List<String>): DivideResult {
        val aria2cList = LinkedList<String>()
        val transmissionList = LinkedList<String>()
        ids.forEach { id ->
            if (downloaderMap[id] == transmissionClient)
                transmissionList.add(id)
            else
                aria2cList.add(id)
        }
        return DivideResult(aria2cList, transmissionList)
    }
}



