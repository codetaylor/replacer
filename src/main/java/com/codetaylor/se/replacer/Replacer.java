package com.codetaylor.se.replacer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Replacer {

  private static final String USAGE = "replacer <data_model> <template_folder> <output_folder> <templates>...";

  public static void main(String[] args) throws IOException, TemplateException {

    if (args.length < 4) {
      System.out.println(USAGE);
      return;
    }

    Path dataModelPath = Paths.get(args[0]);
    Path templateFolderPath = Paths.get(args[1]);
    Path outputFolderPath = Paths.get(args[2]);

    Gson gson = new GsonBuilder().create();
    Map<String, Object> dataModel;

    try (FileReader reader = new FileReader(dataModelPath.toFile())) {
      dataModel = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
        //
      }.getType());
    }

    Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
    cfg.setDirectoryForTemplateLoading(templateFolderPath.toFile());
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setLogTemplateExceptions(false);
    cfg.setWrapUncheckedExceptions(true);
    cfg.setFallbackOnNullLoopVariable(false);
    cfg.setAutoFlush(true);

    for (int i = 3; i < args.length; i++) {
      String templateFilenameString = args[i];
      Path outputFilePath = outputFolderPath.resolve(templateFilenameString);
      Files.createDirectories(outputFilePath.toAbsolutePath().getParent());
      Template template = cfg.getTemplate(Paths.get(templateFilenameString).toString());
      FileWriter fileWriter = new FileWriter(outputFilePath.toFile());
      template.process(dataModel, fileWriter);
      fileWriter.close();
      System.out.println("Created " + outputFilePath);
    }
  }
}