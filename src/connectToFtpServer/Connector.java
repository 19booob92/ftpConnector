package connectToFtpServer;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class Connector {
	static void listDirectory(FTPClient ftpClient, String parentDir, String currentDir, int level) throws IOException {
		String dirToList = parentDir;
		if (!currentDir.equals("")) {
			dirToList += "/" + currentDir;
		}
		FTPFile[] subFiles = ftpClient.listFiles(dirToList);
		if (subFiles != null && subFiles.length > 0) {
			for (FTPFile aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals("..")) {
					continue;
				}
				for (int i = 0; i < level; i++) {
					System.out.print("\t");
				}
				if (aFile.isDirectory()) {
					System.out.println("[" + currentFileName + "]");
					listDirectory(ftpClient, dirToList, currentFileName, level + 1);
				} else {
					System.out.println(currentFileName);
				}
			}
		}
	}

	public static void main(String[] args) {
		String server = "localhost";
		int port = 21;
		String user = "booob";
		String pass = "bob1992";
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(server, port);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Connect failed");
				return;
			}
			boolean success = ftpClient.login(user, pass);
			if (!success) {
				System.out.println("Could not login to the server");
				return;
			}
			String dirToList = "/home/booob";
			listDirectory(ftpClient, dirToList, "", 0);
		} catch (IOException ex) {
			System.out.println("Oops! Something wrong happened");
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
