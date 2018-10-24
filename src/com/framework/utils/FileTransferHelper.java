package com.framework.utils;



//import org.apache.commons.vfs.FileObject;
//import org.apache.commons.vfs.FileSystemOptions;
//import org.apache.commons.vfs.Selectors;
//import org.apache.commons.vfs.impl.StandardFileSystemManager;
//import org.apache.commons.vfs.provider.sftp.SftpFileSystemConfigBuilder;

public class FileTransferHelper {

	public static void main(String[] args) throws Exception {
		String FTP_URL = "qa"; // ftp.xyz.com
		String FTP_USERNAME = "tester"; // ftp_user_01
		String FTP_PASSWORD = "tester"; // secret_123
		String FTP_REMOTE_PATH = "/opt";
		String FTP_LOCAL_PATH = "D:/tempp";
		SFTPuploadFolder(FTP_URL, FTP_USERNAME, FTP_PASSWORD, FTP_LOCAL_PATH, FTP_REMOTE_PATH);
	}
	
	public static void SFTPuploadFolder(String FTP_URL, String FTP_USERNAME, String FTP_PASSWORD, String FTP_LOCAL_PATH, String FTP_REMOTE_PATH)
			throws Exception {
		
//		File local = new File(FTP_LOCAL_PATH);
//		String local_folder_name = local.getName();		
//		
//		StandardFileSystemManager manager = new StandardFileSystemManager();
//		try {
//						
//			String sftpUri = "sftp://" + FTP_USERNAME + ":" + FTP_PASSWORD + "@" + FTP_URL +  FTP_REMOTE_PATH;
//
//			FileSystemOptions opts = new FileSystemOptions();
//			SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
//					opts, "no");
//
//			manager.init();
//
//			FileObject fileObject = manager.resolveFile(sftpUri + "/" + local_folder_name, opts);
//
//			FileObject localFileObject = manager.resolveFile(FTP_LOCAL_PATH);
//
//			fileObject.copyFrom(localFileObject, Selectors.SELECT_ALL);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			manager.close();
//		}
		
	}
}
