package com.framework.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FileSystemHelper {

    public static void copyFile(String p_srcFileName, String p_dstFileName) throws Exception {
	File old_file = new File(p_srcFileName);
	if (old_file.exists()) {
	    File new_file = new File(p_dstFileName);
	    FileUtils.copyFile(old_file, new_file);
	    if (!new_file.exists())
		throw new Exception(p_srcFileName + " wasn't coppied to " + p_dstFileName);
	} else {
	    throw new Exception(p_srcFileName + " doesn't exists");
	}
    }

    public static void copyFolder(String srcDir, String destDir) throws Exception {
	File old_folder = new File(srcDir);
	if (old_folder.exists()) {
	    File new_folder = new File(destDir);
	    FileUtils.copyDirectoryToDirectory(old_folder, new_folder);
	    if (!new_folder.exists())
		throw new Exception(srcDir + " wasn't coppied to " + new_folder);
	} else {
	    throw new Exception(srcDir + " doesn't exists");
	}
    }

    public static void moveFile(String p_srcFileName, String p_dstFileName) throws Exception {
	File old_file = new File(p_srcFileName);
	if (old_file.exists()) {
	    File new_file = new File(p_dstFileName);
	    FileUtils.moveFile(old_file, new_file);
	    if (!new_file.exists())
		throw new Exception(p_srcFileName + " wasn't moved to " + p_dstFileName);
	} else {
	    throw new Exception(p_srcFileName + " doesn't exists");
	}
    }

    public static void moveFolder(String srcDir, String destDir) throws Exception {
	File old_folder = new File(srcDir);
	if (old_folder.exists()) {
	    File new_folder = new File(destDir);
	    FileUtils.moveDirectoryToDirectory(old_folder, new_folder, true);
	    if (!new_folder.exists())
		throw new Exception(srcDir + " wasn't moved to " + destDir);
	} else {
	    throw new Exception(srcDir + " doesn't exists");
	}
    }

    public static void renameFile(String oldFileName, String newFileName) throws Exception {
	File old_file = new File(oldFileName);
	if (old_file.exists()) {
	    File new_file = new File(newFileName);
	    if (!old_file.renameTo(new_file))
		throw new Exception(oldFileName + " wasn't renamed to " + newFileName);
	} else {
	    throw new Exception(oldFileName + " doesn't exists");
	}
    }

    public static String CreateFolder(String rootfolderName, String newFolderName) throws Exception {
	File root = new File(rootfolderName);
	if (!root.exists())
	    throw new Exception("root folder " + rootfolderName + " doesn't exists");

	File new_folder = new File(rootfolderName + newFolderName);
	new_folder.mkdir();
	if (new_folder.exists())
	    return new_folder.getAbsolutePath();
	else
	    throw new Exception("folder wasn't created " + new_folder.getAbsolutePath());
    }

    public static void ChangeLastModifyTimeToCurrent(String fileName) throws InterruptedException, IOException {
	File file = new File(fileName);
	StringBuilder cmd = new StringBuilder();
	cmd = cmd.append("cmd /c (cd /d \"" + file.getParent().trim() + "\"").append(") && (").append("copy \"" + file.getName() + "\" /b+)");
	Runtime.getRuntime().exec(cmd.toString()).waitFor();
    }

    public static void CreateEcxelFileFromTemplate(String fileName) throws Exception {
	File template = new File("C:\\Windows\\ShellNew\\EXCEL12.XLSX");
	copyFile(template.getAbsolutePath().trim(), fileName);
    }

    public static int CountFilesInFolders(String[] folders) throws Exception {
	int ret = 0;
	for (String f : folders) {
	    ret += CountFilesInFolder(new File(f));
	}
	return ret;
    }

    public static int CountFilesInFolder(File folder) throws Exception {
	if (!folder.exists())
	    throw new Exception(folder + " doesn't exists");
	File[] list = folder.listFiles();
	int ret = 0;
	if (list.length != 0) {
	    for (File f : list) {
		if (f.isDirectory()) {
		    ret += CountFilesInFolder(f);
		} else {
		    ret++;
		}
	    }
	    ret++;// root folder should be counted too
	    return ret;
	} else {
	    return 1;
	}
    }

    public static void normalizeFolder(File root, int level) throws IOException {

	while (true) {
	    boolean flag = true;
	    Collection<File> list = FileUtils.listFilesAndDirs(root, FileFilterUtils.directoryFileFilter(), TrueFileFilter.INSTANCE);

	    for (File f : list) {
		// if(f.list().length == 0) {
		// FileUtils.forceDelete(f);
		// continue;
		// }
		String parents = f.getAbsolutePath().replace(root.getAbsolutePath(), "").replace(f.getName(), "");
		if ((parents.split("\\\\").length - 1) > level) {
		    File dest_dir = new File(root.getAbsolutePath().trim() + "\\" + KeywordGenerator.getRandomName());
		    try {
			FileUtils.moveDirectory(f, dest_dir);
			Thread.sleep(100);
		    } catch (Exception e) {
			System.out.println(e.getMessage());
		    }

		    flag = false;
		    break;
		}
	    }
	    if (flag)
		break;
	}

	// printFolders(root);
    }

    public static void normalizeFolders(String[] folders, int level) throws IOException {
	for (String f : folders) {
	    normalizeFolder(new File(f), level);
	    System.out.println("===============================");
	}
    }

    public static void printFolders(File folder) throws IOException {
	Collection<File> list = FileUtils.listFilesAndDirs(folder, FileFilterUtils.directoryFileFilter(), TrueFileFilter.INSTANCE);
	for (File f : list) {
	    System.out.println(f.getAbsolutePath());
	}
    }

    /**
     * Delete folder or file
     * 
     * @param fileName
     *            - folder or file to delete
     * @throws Exception
     *             if file doesn't exists or was not deleted
     */
    public static void Delete(String fileName) throws Exception {
	File old_file = new File(fileName);
	if (old_file.exists()) {
	    if (old_file.isDirectory())
		FileUtils.deleteDirectory(old_file);
	    else {
		if (!old_file.delete())
		    throw new Exception(fileName + " wasn't deleted");
	    }
	} else {
	    throw new Exception(fileName + " doesn't exists");
	}
    }

    // ===============================================================

    public static String getRandomFile(String[] rootFolders, String[] ext, List<String> black_list_ext) throws Exception {
	List<String> ret = getRandomFiles(rootFolders, ext, black_list_ext, 1);
	if (ret == null)
	    throw new Exception("error occured during search random file with ext " + ext);
	else
	    return ret.get(0);
    }

    public static String getRandomFile(String[] rootFolders, String[] ext, List<String> black_list_ext, List<String> black_list_files) throws Exception {
	List<?> ret = getRandomFiles(rootFolders, ext, black_list_ext, -1, black_list_files);
	if (ret == null)
	    throw new Exception("error occured during search random file with ext " + ext);
	else
	    return (String) ret.get(0);
    }

    public static List<String> getRandomFiles(IOFileFilter fileFilter, String[] rootFolders, int count) {

	List<File> all_files = new ArrayList<File>();

	for (String r : rootFolders) {
	    File root = new File(r);
	    all_files.addAll(FileUtils.listFiles(root, fileFilter, TrueFileFilter.INSTANCE));
	    System.out.println(r + "red");
	}

	if (all_files.size() == 0)
	    return null;

	Collections.shuffle(all_files);

	List<String> ret = new ArrayList<String>();
	if (all_files.size() <= count || count == -1) {
	    for (File str : all_files) {

		ret.add(str.getAbsolutePath());

	    }
	} else {
	    for (int i = 0; i < count; i++) {

		ret.add(all_files.get(i).getAbsolutePath());

	    }
	}
	return ret;
    }

    public static List<String> getRandomFiles(String[] rootFolders, String[] ext, List<String> black_list_ext, int count) {

	AndFileFilter aff = new AndFileFilter();

	if (ext != null) {
	    if (ext.length > 0) {
		String[] ext_with_dot = new String[ext.length];
		for (int i = 0; i < ext.length; i++) {
		    ext_with_dot[i] = "." + ext[i];
		}
		aff.addFileFilter(new SuffixFileFilter(ext_with_dot));
	    }
	} else {
	    // System.out.println("ext filter created");
	    if (black_list_ext != null) {
		// int counter = 0;
		int pack_size = 500;
		int count_ext = black_list_ext.size();
		OrFileFilter ttt = new OrFileFilter();
		for (int i = 0; i < count_ext; i += pack_size + 1) {
		    if ((count_ext - i) > pack_size) {
			ttt.addFileFilter(new SuffixFileFilter(black_list_ext.subList(i, i + pack_size)));
		    } else {
			ttt.addFileFilter(new SuffixFileFilter(black_list_ext.subList(i, black_list_ext.size())));
		    }
		    // counter++;
		}
		aff.addFileFilter(new NotFileFilter(ttt));
	    }
	    // System.out.println("black_list_ext filter created " + counter);
	}

	aff.addFileFilter(HiddenFileFilter.VISIBLE);

	return getRandomFiles(aff, rootFolders, count);
    }

    public static List<?> getRandomFiles(String[] rootFolders, String[] ext, List<String> black_list_ext, int count, List<String> black_list_files) {

	List<String> list1 = getRandomFiles(rootFolders, ext, black_list_ext, count);

	return ListUtils.subtract(list1, black_list_files);
    }

    public static String getRandomFolder(String[] rootFolders, boolean useRootFolders) {
	File root = new File(rootFolders[new Random(new Date().getTime()).nextInt(rootFolders.length)]);
	List<File> list = (List<File>) FileUtils.listFilesAndDirs(root, FileFilterUtils.directoryFileFilter(), TrueFileFilter.INSTANCE);

	if (!useRootFolders) {
	    for (String str : rootFolders) {
		list.remove(new File(str));
	    }
	}

	String ret = list.get(new Random().nextInt(list.size())).getAbsolutePath();
	if (!ret.endsWith(":\\")) {
	    ret = ret + "\\";
	}
	return ret;
    }

    public static String getRandomFileName(String[] rootFolders, boolean useRootFolders, String extension) throws Exception {
	String ret = getRandomFolder(rootFolders, useRootFolders) + KeywordGenerator.getRandomName();
	if (extension != null) {
	    ret += "." + extension;
	}
	return ret;
    }

    // =======================================================

    public static String[] SubstractArrays(List<String> array1, String[] array2) {
	List<String> list_array2 = Arrays.asList(array2);
	List<String> ret = new ArrayList<String>();
	for (String r1 : array1) {
	    if (!list_array2.contains(r1))
		ret.add(r1);
	}
	return ret.toArray(new String[1]);
    }

    // =======================================================
    public static void mapNetworkDrive(String letter, String net_path) throws Exception {
	CommandLine cmdLine = new CommandLine("net");
	cmdLine.addArgument("use");
	cmdLine.addArgument(letter + ":");
	cmdLine.addArgument(net_path);
	cmdLine.addArgument("/persistent:yes");
	System.out.println(cmdLine.toString());
	DefaultExecutor executor = new DefaultExecutor();
	// executor.setExitValue(0);
	ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
	executor.setWatchdog(watchdog);
	int exitValue = executor.execute(cmdLine);
	System.out.println(exitValue);
    }

    public static void unMapNetworkDrive(String letter) throws Exception {
	CommandLine cmdLine = new CommandLine("net");
	cmdLine.addArgument("use");
	cmdLine.addArgument(letter + ":");
	cmdLine.addArgument("/delete");
	System.out.println(cmdLine.toString());
	DefaultExecutor executor = new DefaultExecutor();
	// executor.setExitValue(0);
	ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
	executor.setWatchdog(watchdog);
	int exitValue = executor.execute(cmdLine);
	System.out.println(exitValue);
    }
}
