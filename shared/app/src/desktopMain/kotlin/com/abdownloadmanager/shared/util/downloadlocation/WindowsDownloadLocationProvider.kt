package com.abdownloadmanager.shared.util.downloadlocation

import com.sun.jna.platform.win32.KnownFolders
import com.sun.jna.platform.win32.Shell32
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.ptr.PointerByReference
import ir.amirab.util.logger.appLogger
import java.io.File

class WindowsDownloadLocationProvider : DesktopDownloadLocationProvider() {
    override fun getCurrentDownloadLocation(): File? {
        // Diagnostic: check temp dir state right before JNA is used
        val tmpDir = System.getProperty("java.io.tmpdir")
        val tmpFile = File(tmpDir)
        appLogger.i { "DIAGNOSTIC: [WindowsDownloadLocationProvider] Before JNA init" }
        appLogger.i { "DIAGNOSTIC: java.io.tmpdir = \"$tmpDir\"" }
        appLogger.i { "DIAGNOSTIC: File(tmpdir).exists() = ${tmpFile.exists()}" }
        appLogger.i { "DIAGNOSTIC: File(tmpdir).isDirectory() = ${tmpFile.isDirectory()}" }
        appLogger.i { "DIAGNOSTIC: File(tmpdir).canWrite() = ${tmpFile.canWrite()}" }
        appLogger.i { "DIAGNOSTIC: File(tmpdir).toString() = \"${tmpFile}\"" }
        appLogger.i { "DIAGNOSTIC: Thread = ${Thread.currentThread().name}" }
        // Test JNA-style subdirectory creation
        val jnaSubdir = File(tmpFile, "jna-diag-test")
        val mkdirResult = jnaSubdir.mkdirs()
        appLogger.i { "DIAGNOSTIC: jnaSubdir.mkdirs() = $mkdirResult" }
        appLogger.i { "DIAGNOSTIC: jnaSubdir.exists() = ${jnaSubdir.exists()}" }
        appLogger.i { "DIAGNOSTIC: jnaSubdir.canWrite() = ${jnaSubdir.canWrite()}" }
        jnaSubdir.delete()

        val pathRef = PointerByReference()
        val hr = Shell32.INSTANCE.SHGetKnownFolderPath(KnownFolders.FOLDERID_Downloads, 0, WinNT.HANDLE(), pathRef)
        if (hr.toInt() != 0) {
            throw RuntimeException("Failed to get Downloads folder (HRESULT=${hr.toInt()})")
        }
        val downloadsPath = pathRef.value.getWideString(0)
        return File(downloadsPath).canonicalFile
    }
}
