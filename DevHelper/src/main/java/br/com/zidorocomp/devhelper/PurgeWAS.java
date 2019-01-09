package br.com.zidorocomp.devhelper;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PurgeWAS {

	private final String wasPath = "D:/desenv/IBM/WASDEV85/AppServer";

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

		File wasDir = new File(wasPath);
		if (wasDir != null && wasDir.isDirectory()) {

			File profilesPath = new File(wasDir.getAbsolutePath() + "/profiles");
			for (File appServer : profilesPath.listFiles()) {

				clearLogs(appServer);
				clearDeployRecords(appServer);
			}
		} else {
			System.out.println("invalid wasPath! " + wasPath);
		}
	}
	
	private void clearDeployRecords(File appServer) {

		File cellsDir = new File(appServer.getAbsolutePath() + "/config/cells");
		File tempCellsDir = new File(appServer.getAbsolutePath() + "/config/temp/download/cells");

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
	}

	private void clearLogs(File appServer) {

		File logDir = new File(appServer.getAbsolutePath() + "/logs");
		File tranlogDir = new File(appServer.getAbsolutePath() + "/tranlog");
		File tempDir = new File(appServer.getAbsolutePath() + "/temp");
		File wsTempDir = new File(appServer.getAbsolutePath() + "/wstemp");

		for (File logFile : logDir.listFiles()) {
			rmDir(logFile);
		}

		for (File logFile : tranlogDir.listFiles()) {
			rmDir(logFile);
		}

		for (File logFile : tempDir.listFiles()) {
			rmDir(logFile);
		}

		for (File logFile : wsTempDir.listFiles()) {
			rmDir(logFile);
		}
	}

	private void rmDir(File dir) {

		for (File f : dir.listFiles())
			if (!f.delete())
				rmDir(f);

		if (!dir.delete())
			rmDir(dir);
	}

	public static void main(String[] args) throws Exception {
		PurgeWAS p = new PurgeWAS();
		p.purge();
	}
}
