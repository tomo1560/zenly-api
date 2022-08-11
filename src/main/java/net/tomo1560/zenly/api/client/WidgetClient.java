package net.tomo1560.zenly.api.client;

import com.squareup.okhttp.*;
import net.tomo1560.zenly.api.UserLocation;
import net.tomo1560.zenly.api.ZenlyProtos;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class WidgetClient {
    private final OkHttpClient client;
    private final String token;

    private static final String BASE_URL = "https://api.znly.co/widgets";
    private static final String USER_AGENT = "WidgetsExtension/220214183253 CFNetwork/1331.0.7 Darwin/21.4.0";

    public WidgetClient(OkHttpClient client, String token) {
        this.client = client;
        this.token = token;
    }

    public CompletableFuture<UserLocation> fetchUserLocationAsync(String userId) {
        Request request = createGetRequest("pincontext/" + userId + "?preview=0");
        CompletableFuture<Response> future = new CompletableFuture<>();
        client.newCall(request).enqueue(toCallback(future));
        return future.thenApplyAsync(response -> {
            UserLocation result = null;
            try (ResponseBody body = response.body()){
                ZenlyProtos.UserLocation userLocation = ZenlyProtos.UserLocation.parseFrom(body.bytes());
                result = new UserLocation(userId, userLocation.getUser().getLocation().getLatitude(), userLocation.getUser().getLocation().getLongitude());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        });
    }

    public Callback toCallback(CompletableFuture<Response> future) {
        return new Callback() {
            /**
             * Called when the request could not be executed due to cancellation, a
             * connectivity problem or timeout. Because networks can fail during an
             * exchange, it is possible that the remote server accepted the request
             * before the failure.
             *
             * @param request
             * @param e
             */
            @Override
            public void onFailure(Request request, IOException e) {
                future.completeExceptionally(e);
            }

            /**
             * Called when the HTTP response was successfully returned by the remote
             * server. The callback may proceed to read the response body with {@link
             * Response#body}. The response is still live until its response body is
             * closed with {@code response.body().close()}. The recipient of the callback
             * may even consume the response body on another thread.
             *
             * <p>Note that transport-layer success (receiving a HTTP response code,
             * headers and body) does not necessarily indicate application-layer
             * success: {@code response} may still indicate an unhappy HTTP response
             * code like 404 or 500.
             *
             * @param response
             */
            @Override
            public void onResponse(Response response) throws IOException {
                future.complete(response);

            }
        };
    }


    private Request createGetRequest(String verb) {
        return new Request.Builder()
                .url(BASE_URL + "/" + verb)
                .get()
                .headers(getHeaders())
                .build();
    }

    private Headers getHeaders() {
        return new Headers.Builder()
                .add("User-Agent", USER_AGENT)
                .add("Connection", "keep-alive")
                .add("Accept", "*/*")
                .add("Accept-Language", "ja-jp")
                .add("Authorization", "Token " + token)
                .build();
    }
}
