package br.com.zidorocomp.devhelper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WASFriend {

	private final String wasPath = "D:/desenv/IBM/WASDEV7/WebSphere/AppServer";
//	private final String wasPath = "D:/desenv/IBM/WASDEV85/AppServer";

	private final List<String> wasApps = Arrays.asList(
			".*commsvc.*",
			".*DefaultApplication.*",
			".*ibmasyncrsp.*",
			".*IBMUTC.*",
			".*isclite.*",
			".*ivtApp.*",
			".*query.*",
			".*WebSphereWSDM.*");

	public void purge() throws Exception {
		
		System.out.println("\nPurging was... wasPath: " + wasPath+ "\n");

		File wasDir = new File(wasPath);
		if (wasDir != null && wasDir.isDirectory()) {

			File profilesPath = new File(wasDir.getAbsolutePath() + "/profiles");
			for (File appServer : profilesPath.listFiles()) {
				this.clearLogs(appServer);
				this.clearDeployRecords(appServer);
			}
			System.out.println("Yeah, was purged! \n");
		} else {
			System.out.println("invalid wasPath! " + wasPath);
		}
	}

	public void deployApp() throws IOException {

		System.out.println("\nInit fast deploy... wasPath: " + wasPath + "\n");

		File wasDir = new File(wasPath);
		if (wasDir != null && wasDir.isDirectory()) {

			File profilesPath = new File(wasDir.getAbsolutePath() + "/profiles");
			String option = null;
			String selectedProfile = null;
			String selectedCell = null;
			String selectedNode = null;
			String selectedServer = null;
			String appPath = null;

			while (option == null) {

				System.out.println("Please, select one profile:");
				for (int i = 0; i < profilesPath.list().length; i++) {
					System.out.println((i + 1) + " - " + profilesPath.list()[i]);
				}

				System.out.println((profilesPath.list().length + 1) + " - Return");
				option = Utils.readInput();

				if (option.equals(String.valueOf((profilesPath.list().length + 1)))) {
					System.out.println();
					return;
				}

				selectedProfile = profilesPath.list()[Integer.valueOf(option) - 1];
				System.out.println("Good! Select one of this cells:");

				File cellsDir = new File(profilesPath.getAbsolutePath() + "/" + selectedProfile + "/config/cells");
				for (int i = 0; i < cellsDir.list().length; i++) {
					System.out.println((i + 1) + " - " + cellsDir.list()[i]);
				}

				System.out.println((cellsDir.list().length + 1) + " - Return");
				option = Utils.readInput();

				if (option.equals(String.valueOf((cellsDir.list().length + 1)))) {
					System.out.println();
					return;
				}
				
				selectedCell = cellsDir.list()[Integer.valueOf(option) - 1];
				System.out.println("Ok, Now select the node: ");
				
				File nodesDir = new File(cellsDir.getAbsolutePath() + "/" + selectedCell + "/nodes");
				for (int i = 0; i < nodesDir.list().length; i++) {
					System.out.println((i + 1) + " - " + nodesDir.list()[i]);
				}

				System.out.println((nodesDir.list().length + 1) + " - Return");
				option = Utils.readInput();

				if (option.equals(String.valueOf((nodesDir.list().length + 1)))) {
					System.out.println();
					return;
				}
				
				selectedNode = nodesDir.list()[Integer.valueOf(option) - 1];
				System.out.println("And the server: ");
				
				File serversDir = new File(nodesDir.getAbsolutePath() + "/" + selectedNode + "/servers");
				for (int i = 0; i < serversDir.list().length; i++) {
					System.out.println((i + 1) + " - " + serversDir.list()[i]);
				}

				System.out.println((serversDir.list().length + 1) + " - Return");
				option = Utils.readInput();

				if (option.equals(String.valueOf((serversDir.list().length + 1)))) {
					System.out.println();
					return;
				}
				
				selectedServer = serversDir.list()[Integer.valueOf(option) - 1];
				System.out.println("Now, for finish, insert the complete path of yor app: \n(ex: D:/Workspaces/target/appTop.ear)");
				appPath = Utils.readInput();
				
				System.out.println("\nReady! Review the complete configuration: ");
				System.out.println("Profile: " + selectedProfile + 
						"\nCell: " + selectedCell + 
						"\nNode: " + selectedNode+ 
						"\nServer: " + selectedServer + 
						"\nAppPath: " + appPath);
			}
			
			this.deploy(selectedProfile, selectedCell, selectedNode, selectedServer, appPath);
			System.out.println("Deploy successfully! =) \n");
		} else {
			System.out.println("invalid wasPath! " + wasPath);
		}
	}
	
	private void deploy(String profile, String cell, String node, String server, String appPath) throws IOException {
		
//        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",  "cd " + wasPath + "/profiles/" + profile + "/bin && wsadmin");
//        
//        Process p = builder.start();
//        
//        System.out.println(IOUtils.toString(p.getInputStream(), "UTF-8"));
//        
//		System.out.println();
		
	}
	
	private void clearLogs(File appServer) throws Exception {

		if (appServer.isDirectory()) {

			File logDir = new File(appServer.getAbsolutePath() + "/logs");
			File tranlogDir = new File(appServer.getAbsolutePath() + "/tranlog");
			File binLogDir = new File(appServer.getAbsolutePath() + "/bin/logs/app");
			File tempDir = new File(appServer.getAbsolutePath() + "/temp");
			File wsTempDir = new File(appServer.getAbsolutePath() + "/wstemp");

			for (File logFile : logDir.listFiles()) {
				rmDir(logFile);
			}

			for (File logFile : tranlogDir.listFiles()) {
				rmDir(logFile);
			}
			
			if (binLogDir.listFiles() != null) {
				for (File logFile : binLogDir.listFiles()) {
					rmDir(logFile);
				}
			}

			for (File tempFile : tempDir.listFiles()) {
				rmDir(tempFile);
			}

			for (File tempFile : wsTempDir.listFiles()) {
				rmDir(tempFile);
			}
		}
	}
	
	private void clearDeployRecords(File appServer) throws Exception {
		
		if(appServer.isDirectory()) {

			File cellsDir = new File(appServer.getAbsolutePath() + "/config/cells");
			File tempCellsDir = new File(appServer.getAbsolutePath() + "/config/temp/download/cells");
			File installedAppsDir = new File(appServer.getAbsolutePath() + "/installedApps");
	
			for (File cellNodeDir : cellsDir.listFiles()) {
	
				for (File deployedApp : new File(cellNodeDir.getAbsolutePath() + "/applications").listFiles()) {
					if (!wasApps.stream().filter(wasApp -> deployedApp.getName().matches(wasApp)).findFirst().isPresent()) {
						rmDir(deployedApp);
					}
				}
	
				for (File deployedApp : new File(cellNodeDir.getAbsolutePath() + "/blas").listFiles()) {
					if (!wasApps.stream().filter(wasApp -> deployedApp.getName().matches(wasApp)).findFirst().isPresent()) {
						rmDir(deployedApp);
					}
				}
	
				for (File deployedApp : new File(cellNodeDir.getAbsolutePath() + "/cus").listFiles()) {
					if (!wasApps.stream().filter(wasApp -> deployedApp.getName().matches(wasApp)).findFirst().isPresent()) {
						rmDir(deployedApp);
					}
				}
			}
	
			for (File cellNodeDir : tempCellsDir.listFiles()) {
	
				for (File deployedApp : new File(cellNodeDir.getAbsolutePath() + "/applications").listFiles()) {
					if (!wasApps.stream().filter(wasApp -> deployedApp.getName().matches(wasApp)).findFirst().isPresent()) {
						rmDir(deployedApp);
					}
				}
	
				for (File deployedApp : new File(cellNodeDir.getAbsolutePath() + "/blas").listFiles()) {
					if (!wasApps.stream().filter(wasApp -> deployedApp.getName().matches(wasApp)).findFirst().isPresent()) {
						rmDir(deployedApp);
					}
				}
	
				for (File deployedApp : new File(cellNodeDir.getAbsolutePath() + "/cus").listFiles()) {
					if (!wasApps.stream().filter(wasApp -> deployedApp.getName().matches(wasApp)).findFirst().isPresent()) {
						rmDir(deployedApp);
					}
				}
			}
			
			for (File cellNodeDir : installedAppsDir.listFiles()) {
				for (File deployedApp : cellNodeDir.listFiles()) {
					if (!wasApps.stream().filter(wasApp -> deployedApp.getName().matches(wasApp)).findFirst().isPresent()) {
						rmDir(deployedApp);
					}
				}
			}
		}
	}

	private void rmDir(File dir) throws Exception {

		if (dir.isDirectory())
			for (File f : dir.listFiles())
				if (!f.delete())
					rmDir(f);

		//precisa disso?
		if (!dir.delete())
			rmDir(dir);
	}
}
