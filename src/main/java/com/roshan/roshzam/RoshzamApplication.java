package com.roshan.roshzam;

import com.roshan.roshzam.services.FingerPrintingService;
import com.roshan.roshzam.util.FileUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@SpringBootApplication
public class RoshzamApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoshzamApplication.class, args);
	}

	private final static String SAMPLES_DIRECTORY = "samples";

	@Autowired
	FingerPrintingService fingerPrintingService;

	@Bean
	public CommandLineRunner setupDB() {
		// Memory inefficient file stream - could use DirectoryStream in the future
		return args -> {
			File[] originalAudioFiles = new File(SAMPLES_DIRECTORY).listFiles();
			if(originalAudioFiles == null || originalAudioFiles.length == 0) {
				return;
			}

			Stream.of(originalAudioFiles)
					.forEach(audioFile -> {
						// Need to pre-process file name to remove spaces
						String originalFilename = audioFile.getName();
						String sanitisedFilename = originalFilename.replaceAll("\\s+", "_");
						System.out.printf("Processing %s \n", originalFilename);

						final File tempFileForProcessing;
						try {
							// An expensive operation but enables me to create a new file where I can alter the filename
							// without damaging the original contents
							tempFileForProcessing = FileUtility.copyToTempFile(audioFile, sanitisedFilename);
						} catch (IOException e) {
							System.out.printf("Failed processing %s \n", originalFilename);
							throw new RuntimeException(e);
						}

						fingerPrintingService.storeAudioFingerPrint(tempFileForProcessing, sanitisedFilename);

						System.out.printf("Finished processing %s \n", originalFilename);
					});
		};
	}

}
