package com.framework.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;


public class SoupUiHelper {

    public boolean runSoupUItestcase(String project_folder, String project_file, String testcase_name, Properties params, String endpoint) throws ExecuteException, IOException {
        //сохраняем параметры для тесткейса в файл.
        //внутри тесткейса есть шаг, в котором из этого файла параметры будут загружены

        File p = new File(project_folder, testcase_name + ".props");
        waitFor(p, 30);//если файл есть, ждём, пока он не пропадёт, или сваливаемся по таймауту
        FileWriter fw = new FileWriter(p);
        params.store(fw, "updated via autotests");
        fw.flush();
        fw.close();

        //формируем командную строку для запуска одного теста через SoupUI testrunner
        Map<String, Object> map = new HashMap<>();
        map.put("project", new File(project_folder, project_file));
        map.put("testcase", testcase_name);
        CommandLine cmdLine = new CommandLine("testrunner.bat");
        cmdLine.addArgument("-c").addArgument("${testcase}").addArgument("-e").addArgument(endpoint).addArgument("${project}");
        cmdLine.setSubstitutionMap(map);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(0);//если вернётся 0, то значит тест успешно выполнился
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);//если не выполнится за 60 секунд, то убить процесс и вернуть ошибку
        executor.setWatchdog(watchdog);
        int exitValue = executor.execute(cmdLine);

        p.delete();
//	System.out.println(exitValue);
        return !executor.isFailure(exitValue);
    }

    /**
     * стыреный метод из org.apache.commons.io.FileUtils, работает наоборот, ждёт пока файл не пропадёт
     *
     * @param file
     * @param seconds
     * @return
     */
    public boolean waitFor(final File file, final int seconds) {
        long finishAt = System.currentTimeMillis() + (seconds * 1000L);
        boolean wasInterrupted = false;
        try {
            while (file.exists()) {
                long remaining = finishAt - System.currentTimeMillis();
                if (remaining < 0) {
                    return false;
                }
                try {
                    Thread.sleep(Math.min(100, remaining));
                } catch (final InterruptedException ignore) {
                    wasInterrupted = true;
                } catch (final Exception ex) {
                    break;
                }
            }
        } finally {
            if (wasInterrupted) {
                Thread.currentThread().interrupt();
            }
        }
        return true;
    }
}