import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.Instant;

/**
 * Created by mromero on 8/1/17.
 */
public class BatchFactory {
    public static void generateBatch() {
        String NULL = "";

        JSONObject batchObject = new JSONObject();

        JSONObject collection = new JSONObject();

        collection.put("statusERD", "new");
        collection.put("lastUpdated", Instant.now().toString());

        JSONArray imageColumns = new JSONArray();
        imageColumns.add("image_1");
        imageColumns.add("image_2");
        imageColumns.add("image_3");
        imageColumns.add("image_4");
        imageColumns.add("image_5");
        imageColumns.add("image_6");
        imageColumns.add("image_7");
        imageColumns.add("image_8");
        imageColumns.add("image_9");
        imageColumns.add("image_10");
        imageColumns.add("image_11");
        imageColumns.add("image_12");
        imageColumns.add("image_13");
        imageColumns.add("image_14");
        imageColumns.add("image_15");
        imageColumns.add("image_16");
        imageColumns.add("image_17");
        imageColumns.add("image_18");
        imageColumns.add("image_19");
        imageColumns.add("image_20");
        imageColumns.add("image_21");
        imageColumns.add("image_22");
        imageColumns.add("image_23");
        imageColumns.add("image_24");
        imageColumns.add("image_25");
        imageColumns.add("image_26");
        imageColumns.add("image_27");
        imageColumns.add("image_28");
        imageColumns.add("image_29");
        imageColumns.add("image_30");
        collection.put("imageColumns", imageColumns);

        collection.put("rowCount", 1);
        collection.put("created", Instant.now().toString());

        collection.put("recordSrcMapping", new JSONObject());
        collection.put("name", "automation-testlist");
        collection.put("rowParentCount", 2);
        collection.put("lastRow", 1);

        JSONArray recordSrcAttributes = new JSONArray();
        recordSrcAttributes.add("sku");
        recordSrcAttributes.add("title");
        recordSrcAttributes.add("product_type");
        recordSrcAttributes.add("body_html");
        recordSrcAttributes.add("handle");
        recordSrcAttributes.add("option1_name");
        recordSrcAttributes.add("option1_value");
        recordSrcAttributes.add("option2_name");
        recordSrcAttributes.add("option2_value");
        recordSrcAttributes.add("option3_name");
        recordSrcAttributes.add("option3_value");
        recordSrcAttributes.add("tags");
        recordSrcAttributes.add("price");
        recordSrcAttributes.add("compare_at_price");
        recordSrcAttributes.add("taxable");
        recordSrcAttributes.add("vendor");
        recordSrcAttributes.add("position");
        recordSrcAttributes.add("published");
        recordSrcAttributes.add("published_scope");
        recordSrcAttributes.add("template_suffix");
        recordSrcAttributes.add("weight");
        recordSrcAttributes.add("weight_unit");
        recordSrcAttributes.add("grams");
        recordSrcAttributes.add("inventory_management");
        recordSrcAttributes.add("inventory_quantity");
        recordSrcAttributes.add("inventory_policy");
        recordSrcAttributes.add("requires_shipping");
        recordSrcAttributes.add("metafields_global_title_tag");
        recordSrcAttributes.add("metafields_global_description_tag");
        recordSrcAttributes.add("fulfillment_service");
        recordSrcAttributes.add("barcode");
        recordSrcAttributes.add("image_1");
        recordSrcAttributes.add("alt_image_1");
        recordSrcAttributes.add("image_2");
        recordSrcAttributes.add("alt_image_2");
        recordSrcAttributes.add("image_3");
        recordSrcAttributes.add("alt_image_3");
        recordSrcAttributes.add("image_4");
        recordSrcAttributes.add("alt_image_4");
        recordSrcAttributes.add("image_5");
        recordSrcAttributes.add("alt_image_5");
        recordSrcAttributes.add("image_6");
        recordSrcAttributes.add("alt_image_6");
        recordSrcAttributes.add("image_7");
        recordSrcAttributes.add("alt_image_7");
        recordSrcAttributes.add("image_8");
        recordSrcAttributes.add("alt_image_8");
        recordSrcAttributes.add("image_9");
        recordSrcAttributes.add("alt_image_9");
        recordSrcAttributes.add("image_10");
        recordSrcAttributes.add("alt_image_10");
        recordSrcAttributes.add("image_11");
        recordSrcAttributes.add("alt_image_11");
        recordSrcAttributes.add("image_12");
        recordSrcAttributes.add("alt_image_12");
        recordSrcAttributes.add("image_13");
        recordSrcAttributes.add("alt_image_13");
        recordSrcAttributes.add("image_14");
        recordSrcAttributes.add("alt_image_14");
        recordSrcAttributes.add("image_15");
        recordSrcAttributes.add("alt_image_15");
        recordSrcAttributes.add("image_16");
        recordSrcAttributes.add("alt_image_16");
        recordSrcAttributes.add("image_17");
        recordSrcAttributes.add("alt_image_17");
        recordSrcAttributes.add("image_18");
        recordSrcAttributes.add("alt_image_18");
        recordSrcAttributes.add("image_19");
        recordSrcAttributes.add("alt_image_19");
        recordSrcAttributes.add("image_20");
        recordSrcAttributes.add("alt_image_20");
        recordSrcAttributes.add("image_21");
        recordSrcAttributes.add("alt_image_21");
        recordSrcAttributes.add("image_22");
        recordSrcAttributes.add("alt_image_22");
        recordSrcAttributes.add("image_23");
        recordSrcAttributes.add("alt_image_23");
        recordSrcAttributes.add("image_24");
        recordSrcAttributes.add("alt_image_24");
        recordSrcAttributes.add("image_25");
        recordSrcAttributes.add("alt_image_25");
        recordSrcAttributes.add("image_26");
        recordSrcAttributes.add("alt_image_26");
        recordSrcAttributes.add("image_27");
        recordSrcAttributes.add("alt_image_27");
        recordSrcAttributes.add("image_28");
        recordSrcAttributes.add("alt_image_28");
        recordSrcAttributes.add("image_29");
        recordSrcAttributes.add("alt_image_29");
        recordSrcAttributes.add("image_30");
        recordSrcAttributes.add("alt_image_30");
        collection.put("recordSrcAttributes", recordSrcAttributes);

        collection.put("organizationId", "E4N49A4IV9");
        collection.put("map", NULL);
        collection.put("id", "4UJANJSJWV");
        collection.put("type", "erd");

        batchObject.put("collection", collection);

        batchObject.put("batchNumber", 0);
        batchObject.put("totalBatches", 1);

        JSONArray batch = new JSONArray();

        JSONObject batchElement = new JSONObject();
        batchElement.put("lastUpdated", Instant.now().toString());

        JSONObject customFields = new JSONObject();
        customFields.put("body_html", NULL);
        customFields.put("inventory_management", NULL);
        customFields.put("requires_shipping", true);
        customFields.put("option1_name", NULL);
        customFields.put("metafields_global_description_tag", NULL);
        customFields.put("option3_value", NULL);
        customFields.put("alt_image_5", NULL);
        customFields.put("alt_image_4", NULL);
        customFields.put("alt_image_3", NULL);
        customFields.put("option2_name", NULL);
        customFields.put("alt_image_2", NULL);
        customFields.put("alt_image_9", NULL);
        customFields.put("alt_image_8", NULL);
        customFields.put("price", "70");
        customFields.put("alt_image_7", NULL);
        customFields.put("alt_image_6", NULL);
        customFields.put("sku", "155686755");
        customFields.put("grams", NULL);
        customFields.put("alt_image_1", NULL);
        customFields.put("barcode", NULL);
        customFields.put("inventory_quantity", NULL);
        customFields.put("compare_at_price", NULL);
        customFields.put("taxable", true);
        customFields.put("fulfillment_service", "manual");
        customFields.put("weight", NULL);
        customFields.put("handle", NULL);
        customFields.put("published", NULL);
        customFields.put("alt_image_30", NULL);
        customFields.put("tags", NULL);
        customFields.put("published_scope", "global");
        customFields.put("weight_unit", "lb");
        customFields.put("alt_image_29", NULL);
        customFields.put("position", 1);
        customFields.put("alt_image_28", NULL);
        customFields.put("option3_name", NULL);
        customFields.put("alt_image_23", NULL);
        customFields.put("alt_image_22", NULL);
        customFields.put("alt_image_21", NULL);
        customFields.put("alt_image_20", NULL);
        customFields.put("alt_image_27", NULL);
        customFields.put("alt_image_26", NULL);
        customFields.put("alt_image_25", NULL);
        customFields.put("alt_image_24", NULL);
        customFields.put("metafields_global_title_tag", NULL);
        customFields.put("title", "Dining Chair");
        customFields.put("template_suffix", NULL);
        customFields.put("vendor", NULL);
        customFields.put("alt_image_19", NULL);
        customFields.put("alt_image_18", NULL);
        customFields.put("alt_image_17", NULL);
        customFields.put("option1_value", NULL);
        customFields.put("alt_image_12", NULL);
        customFields.put("alt_image_11", NULL);
        customFields.put("alt_image_10", NULL);
        customFields.put("alt_image_16", NULL);
        customFields.put("alt_image_15", NULL);
        customFields.put("alt_image_14", NULL);
        customFields.put("alt_image_13", NULL);
        customFields.put("inventory_policy", "deny");
        customFields.put("product_type", "White Chair");
        customFields.put("option2_value", NULL);
        batchElement.put("customFields", customFields);

        batchElement.put("_row", 0);
        batchElement.put("status", "Active");
        batchElement.put("created", Instant.now().toString());

        JSONObject images = new JSONObject();
        JSONObject imageElement = new JSONObject();
        JSONObject thumbnail = new JSONObject();
        thumbnail.put("url", "http://www.finemodimports.com/v/vspfiles/FMIimages/10014white-01.jpg");
        thumbnail.put("filename", "10014white-01.jpg");
        imageElement.put("thumbnail", thumbnail);

        imageElement.put("id", "LR6XJZCTLX");
        imageElement.put("filename", "10014white-01.jpg");
        imageElement.put("position", 0);
        imageElement.put("url", "http://www.finemodimports.com/v/vspfiles/FMIimages/10014white-01.jpg");
        imageElement.put("fromUrl", true);
        images.put("10014white-01.jpg", imageElement);

        batchElement.put("images", images);
        batchElement.put("sourceRecordId", "FMI10014");
        batchElement.put("id", "ZSLKWJGMDS");
        batchElement.put("collectionId", "4UJANJSJWV");

        batch.add(batchElement);

        batchObject.put("batch", batch);

        batchObject.put("targetUrl", "http://localhost:8080/integrations/".concat(IntegrationTest.IntegrationId.toString()).concat("/items"));
        batchObject.put("createdAt", Instant.now().toString());
        batchObject.put("eventName", "product.created");
        batchObject.put("organizationId", "E4N49A4IV9");
        batchObject.put("subscriptionId", "CREATE10X1");

        String responseFileName = "test_batch_1prod.json";
        Utils.checkIfFileExist(responseFileName);
        Utils.writeResponseFile(responseFileName, batchObject);
    }
}
