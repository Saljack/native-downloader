package cz.saljack.downloader;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import cz.saljack.downloader.service.DownloaderService;

@Component
public class Runner implements ApplicationRunner {

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ApplicationRunner.class);

  @Autowired
  private ObjectProvider<DownloaderService> downloaderService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info(downloaderService.getObject().download().block());
  }

}
