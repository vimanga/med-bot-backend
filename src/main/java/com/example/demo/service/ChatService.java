package com.example.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import opennlp.tools.doccat.BagOfWordsFeatureGenerator;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelUtil;

public class ChatService {
	
	private static Map<String, String> questionAnswer = new HashMap<>();

	/*
	 * Define answers for each given category.
	 */
//	static {
//		questionAnswer.put("greeting", "Hello, how can I help you?");
//		questionAnswer.put("product-inquiry",
//				"Product is a TipTop mobile phone. It is a smart phone with latest features like touch screen, blutooth etc.");
//		questionAnswer.put("price-inquiry", "Price is $300");
//		questionAnswer.put("conversation-continue", "What else can I help you with?");
//		questionAnswer.put("conversation-complete", "Nice chatting with you. Bbye.");
//
//	}
	
	
	////////////////////////////////////////////////////////
	
	//D:\Research\Others projets\Answers_categories.csv
	///home/ec2-user/test/Answers_categories.csv
	
	static {
		String csvFile = "/home/ec2-user/test/Answers_categories.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = "#";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);
                questionAnswer.put(country[0], country[1]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

	}
	
	public String chatOutput(String input, Long patientId, String doctorEmail) throws FileNotFoundException, IOException, InterruptedException{
		
		
		// Train categorizer model to the training data we created.
				DoccatModel model = trainCategorizerModel();

				// Take chat inputs from console (user) in a loop.
//				Scanner scanner = new Scanner(System.in);
//				while (true) {

					// Get chat input from user.
//					System.out.println("##### You:");
//					String userInput = scanner.nextLine();

					// Break users chat input into sentences using sentence detection.
					String[] sentences = breakSentences(input);

					String answer = "";
					boolean conversationComplete = false;

					// Loop through sentences.
					for (String sentence : sentences) {

						// Separate words from each sentence using tokenizer.
						String[] tokens = tokenizeSentence(sentence);

						// Tag separated words with POS tags to understand their gramatical structure.
						String[] posTags = detectPOSTags(tokens);

						// Lemmatize each word so that its easy to categorize.
						String[] lemmas = lemmatizeTokens(tokens, posTags);

						// Determine BEST category using lemmatized tokens used a mode that we trained
						// at start.
						String category = detectCategory(model, lemmas);

						// Get predefined answer from given category & add to answer.
						answer = answer + " " + questionAnswer.get(category);

						// If category conversation-complete, we will end chat conversation.
						if ("conversation-complete".equals(category)) {
							conversationComplete = true;
						}
//						if ("Contact-Doctor".equals(category)) {
//
//							EmailService emailService = new EmailService();
//							try {
//								emailService.sendmail(sentence, patientId, doctorEmail);
//							} catch (AddressException e) {
//								e.printStackTrace();
//							} catch (MessagingException e) {
//								e.printStackTrace();
//							}
//							
//						}
					}

					if (conversationComplete) {
						System.out.println("the end");
					}
					// Print answer back to user. If conversation is marked as complete, then end
					// loop & program.
					System.out.println("##### Chat Bot: " + answer);
					return answer;
					
//					if (conversationComplete) {
//						System.out.println("the end");
//						return answer;
//					}
					
					

				}
				
				

		
	
	///home/ec2-user/test/faq-categorizer.txt
	
	private static DoccatModel trainCategorizerModel() throws FileNotFoundException, IOException {
		// faq-categorizer.txt is a custom training data with categories as per our chat
		// requirements.
		InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File("/home/ec2-user/test/faq-categorizer.txt"));
		ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
		ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

		DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });

		TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
		params.put(TrainingParameters.CUTOFF_PARAM, 0);

		// Train a model with classifications from above file.
		DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);
		return model;
	}

	/**
	 * Detect category using given token. Use categorizer feature of Apache OpenNLP.
	 * 
	 * @param model
	 * @param finalTokens
	 * @return
	 * @throws IOException
	 */
	private static String detectCategory(DoccatModel model, String[] finalTokens) throws IOException {

		int categotyAmount = 0;
		
		// Initialize document categorizer tool
		DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);

		// Get best possible category.
		double[] probabilitiesOfOutcomes = myCategorizer.categorize(finalTokens);
		String category = myCategorizer.getBestCategory(probabilitiesOfOutcomes);
		
		categotyAmount = myCategorizer.getNumberOfCategories();
		
		double max, min;
		boolean hasAnswer = false;
		
		max = (1.0/categotyAmount)+0.07;
		min = (1/categotyAmount)-0.02;
		
		System.out.println("max = "+max);
		
		for(int i=0; i<=(probabilitiesOfOutcomes.length-1); i++) {
						
			System.out.println(myCategorizer.getCategory(i)+" : "+probabilitiesOfOutcomes[i]);
			
			if (max<probabilitiesOfOutcomes[i]) {
				hasAnswer = true;
			}
			
		
		}
		if(hasAnswer == false){
			category = "Contact-Doctor";
		}
		
		System.out.println("has answer : "+ hasAnswer);
		
		System.out.println("Category: " + category);
		
		System.out.println("Category amount = "+ categotyAmount);
		
		
		
		

		return category;

	}

	/**
	 * Break data into sentences using sentence detection feature of Apache OpenNLP.
	 * 
	 * @param data
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String[] breakSentences(String data) throws FileNotFoundException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("/home/ec2-user/test/en-sent.bin")) {

			SentenceDetectorME myCategorizer = new SentenceDetectorME(new SentenceModel(modelIn));

			String[] sentences = myCategorizer.sentDetect(data);
			System.out.println("Sentence Detection: " + Arrays.stream(sentences).collect(Collectors.joining(" | ")));

			return sentences;
		}
	}

	/**
	 * Break sentence into words & punctuation marks using tokenizer feature of
	 * Apache OpenNLP.
	 * 
	 * @param sentence
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String[] tokenizeSentence(String sentence) throws FileNotFoundException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("/home/ec2-user/test/en-token.bin")) {

			// Initialize tokenizer tool
			TokenizerME myCategorizer = new TokenizerME(new TokenizerModel(modelIn));

			// Tokenize sentence.
			String[] tokens = myCategorizer.tokenize(sentence);
			System.out.println("Tokenizer : " + Arrays.stream(tokens).collect(Collectors.joining(" | ")));

			return tokens;

		}
	}

	/**
	 * Find part-of-speech or POS tags of all tokens using POS tagger feature of
	 * Apache OpenNLP.
	 * 
	 * @param tokens
	 * @return
	 * @throws IOException
	 */
	private static String[] detectPOSTags(String[] tokens) throws IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("/home/ec2-user/test/en-pos-maxent.bin")) {

			// Initialize POS tagger tool
			POSTaggerME myCategorizer = new POSTaggerME(new POSModel(modelIn));

			// Tag sentence.
			String[] posTokens = myCategorizer.tag(tokens);
			System.out.println("POS Tags : " + Arrays.stream(posTokens).collect(Collectors.joining(" | ")));

			return posTokens;

		}

	}

	/**
	 * Find lemma of tokens using lemmatizer feature of Apache OpenNLP.
	 * 
	 * @param tokens
	 * @param posTags
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private static String[] lemmatizeTokens(String[] tokens, String[] posTags)
			throws InvalidFormatException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("/home/ec2-user/test/en-lemmatizer.bin")) {

			// Tag sentence.
			LemmatizerME myCategorizer = new LemmatizerME(new LemmatizerModel(modelIn));
			String[] lemmaTokens = myCategorizer.lemmatize(tokens, posTags);
			System.out.println("Lemmatizer : " + Arrays.stream(lemmaTokens).collect(Collectors.joining(" | ")));

			return lemmaTokens;

		}
	}

	

}
