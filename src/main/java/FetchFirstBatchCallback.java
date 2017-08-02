import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.time.LocalDateTime;

import static org.mockserver.model.HttpStatusCode.OK_200;

/**
 * Created by mromero on 7/31/17.
 */
public class FetchFirstBatchCallback implements ExpectationCallback {
    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        HttpResponse httpServerResponse = createHttpServerResponse();
        httpServerResponse.withDelay(Delay.milliseconds(2000));
        return httpServerResponse;
    }

    private HttpResponse createHttpServerResponse() {
        System.out.println("["+LocalDateTime.now()+"] ".concat("Responded for First Batch: ").concat("Batch1"));
        IntegrationTest.readyToSendBatches = true;
        return new HttpResponse()
                .withStatusCode(OK_200.code())
                .withHeaders(
                        new Header("Content-Type", "application/json; charset=utf-8"),
                        new Header("Cache-Control", "public, max-age=86400")
                )
                .withBody(Utils.readResponseFile("fetch_first_batch.json"));
    }
}
