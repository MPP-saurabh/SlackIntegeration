import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class pushFileToSlack {
	static List<String> result1 = new ArrayList<>();

	public static List<File> findDirectoriesWithSameName(String name, File root) {
		List<File> result = new ArrayList<>();

		for (File file : root.listFiles()) {
			if (file.isDirectory()) {
				if (file.getName().startsWith(name)) {
					String fName = file.getName();
					 System.out.println(fName);
					result1.add(fName);
				}

				result.addAll(findDirectoriesWithSameName(name, file));
			}
		}

		return result;
	}

	public static String getDesiredFolder(List<String> Result1, String folderName) {
		System.out.println(result1);
		List<Integer> result2 = new ArrayList<>();
		for (String h : result1) {
			String k = h.replaceAll("[a-z,A-Z,(,)]", "");
			if (k.equals("")) {
				k = "0";
				// System.out.print("0");
				// result2.add(Integer.parseInt(k));
			}
			// System.out.println(k);
			result2.add(Integer.parseInt(k));

		}
		System.out.println(result2);
		int max = Collections.max(result2);
		System.out.println(max);
		return folderName + "(" + max + ")";

	}

	public static void sendFileToChannel(String filePathReciever, String channelReceiver) throws IOException {

		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("text/plain");
		RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("file", filePathReciever,
						RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePathReciever)))
				.addFormDataPart("channels", channelReceiver).build();
		Request request = new Request.Builder().url("https://slack.com/api/files.upload").method("POST", body)
				.addHeader("Authorization",
						"Bearer xoxp-1058001528117-1058001528165-1056794285106-53f6c70d915999c50af5cc78fdcc9063")
				.build();
		Response response = client.newCall(request).execute();

	}

	public static void main(String[] args) {
		File file = new File("C:\\\\Users\\\\Provar Sourav\\\\Documents\\\\ANT");
		String fileName = "Test_Run_Report.pdf";
		String folderNameToBeSearched = "Results";
		String channel = "simple";
		findDirectoriesWithSameName(folderNameToBeSearched, file);
		String outputFolder = getDesiredFolder(result1, folderNameToBeSearched);
		System.out.println(outputFolder);
		String filePath = file.getAbsolutePath() + File.separator + outputFolder + File.separator + fileName;
		System.out.println(filePath);
		try {
			sendFileToChannel(filePath, channel);
			System.out.println("---------------------------------------------------------------------------");
			System.out.println("SUCCESS! File has been succesfully sent to the Slack channel : " + channel);
			System.out.println("---------------------------------------------------------------------------");
		} catch (Exception e) {

			System.err.println("Error: " + e.getMessage());

		}
	}

}
