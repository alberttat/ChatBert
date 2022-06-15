import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

public class APIHandler {
	public static HttpResponse<String> response;
	private static String weatherAPI = "4409172421c171227d363cba52a1ee25";
	private static String youtubeAPI = "AIzaSyDYMvt8DvG_ui4WfKlQHezPNrsKc0vTYik";
	private static HttpRequest request;
	static String client_id = "9f6bc9a3e7c1404e81a2274fd0ac3468";
	static String client_secret = "8f9a608f03da4fcfab6badcf9ffb57d3";
	static String idSecret = client_id + ":" + client_secret;

	private static String spotifyRequestToken() throws IOException, InterruptedException {
		// auth access
		String idSecretEncoded = Base64.getEncoder().encodeToString(idSecret.getBytes());
		String requestAuth = "https://accounts.spotify.com/api/token";
		request = HttpRequest.newBuilder().uri(URI.create(requestAuth))
				.POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials")).headers("Authorization",
						"Basic " + idSecretEncoded, "Content-Type", "application/x-www-form-urlencoded")
				.build();
		response = Server.client.send(request, HttpResponse.BodyHandlers.ofString());
		JSONObject accessToken = new JSONObject(response.body());
		String access_Token = accessToken.getString("access_token");
		return access_Token;

	}

	static void spotify(String spotifySearchString) throws IOException, InterruptedException {

		String encodedString = encode(spotifySearchString);
		String spotifyAPI = "https://api.spotify.com/v1/search?q=" + encodedString + "&type=track,artist&limit=1";
		String access_Token = spotifyRequestToken();

		// get request
		request = HttpRequest.newBuilder().uri(URI.create(spotifyAPI)).GET()
				.header("Authorization", "Bearer " + access_Token).build();

		response = Server.client.send(request, HttpResponse.BodyHandlers.ofString());

	}

	static String parseSpotifyResponse(HttpResponse<String> response) {
		JSONObject spotifyResponseObj = new JSONObject(response.body());
		String track;
		JSONArray musicItems = spotifyResponseObj.getJSONObject("tracks").getJSONArray("items");
		if (musicItems != null && musicItems.length() > 0) {
			track = spotifyResponseObj.getJSONObject("tracks").getJSONArray("items").getJSONObject(0)
					.getJSONObject("external_urls").getString("spotify");
			return track;
		}

		return "Your search did not yield any results, please try the command with another search";

	}

	static void youtube(String youtubeSearchString) throws IOException, InterruptedException {

		String encodedString = encode(youtubeSearchString);
		request = HttpRequest.newBuilder().uri(URI.create("https://www.googleapis.com/youtube/v3/search?key="
				+ youtubeAPI + "&q=" + encodedString + "&type=video&order=viewCount&maxResults=1")).GET().build();

		response = Server.client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	static String parseYoutubeResponse(HttpResponse<String> response) {
		JSONObject youtubeResult = new JSONObject(response.body());
		int resultsPerPage = youtubeResult.getJSONObject("pageInfo").getInt("resultsPerPage");
		if (resultsPerPage != 1) {
			return "Your search did not yield any results, please try the command with another search";
		}
		JSONArray youtubeItems = youtubeResult.getJSONArray("items");
		String id = youtubeItems.getJSONObject(0).getJSONObject("id").getString("videoId");

		return "Highest viewed youtube video containing your search: https://www.youtube.com/watch?v=" + id;
	}

	static void weather(String zipCode) throws IOException, InterruptedException {
		String weather = "https://api.openweathermap.org/data/2.5/weather?zip=" + zipCode + ",us&appid=" + weatherAPI
				+ "&units=imperial";
		request = HttpRequest.newBuilder().uri(URI.create(weather)).GET() // GET is default
				.build();

		response = Server.client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	static String parseWeatherResponse(HttpResponse<String> response) {
		String city = "";
		String weatherDesc = "";
		int weatherFeelsLike, weatherMin, weatherMax;
		JSONObject weather = new JSONObject(response.body());
		JSONArray weathers = weather.getJSONArray("weather");

		for (int i = 0; i < weathers.length(); i++) {
			JSONObject rec = weathers.getJSONObject(i);
			weatherDesc = rec.getString("description");
		}

		city = weather.getString("name");

		JSONObject temps = weather.getJSONObject("main");
		weatherMin = temps.getInt("temp_min");
		weatherMax = temps.getInt("temp_max");
		weatherFeelsLike = temps.getInt("feels_like");

		return city + ": " + weatherDesc + ". Feels like " + weatherFeelsLike + "°F has a high of " + weatherMax
				+ "°F and low of " + weatherMin + "°F";
	}

	// URL friendly strings
	private static String encode(String stringToEncode) {

		String encodedString = "";
		try {
			encodedString = URLEncoder.encode(stringToEncode, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodedString;

	}

}
