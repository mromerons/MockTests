import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Created by mromero on 8/1/17.
 */
public class BatchFactory {
    //
    private static String listName = "automation-test-list";
    private static String organizationId = "E4N49A4IV9";
    private static String collectionId = "XXXXXXXXXX";
    private static String subscriptionId = "ZZZZZZZZZZ";

    //Helpers
    private static String empty = "empty";
    private static boolean hasVariants = false;

    //Initial Values
    private static long sku = 95045641100L;
    private static long title = 1L;
    private static long position = 100L;
    private static long barcode = 95045641100L;

    //Images Map
    private static HashMap imageURL = new HashMap(){{
        put(1, "https://image.freepik.com/free-vector/industry-logo_1103-713.jpg");
        put(2, "https://image.freepik.com/free-vector/online-games-logo_1103-1041.jpg");
        put(3, "https://image.freepik.com/free-vector/solid-technology-logo_1103-819.jpg");
        put(4, "https://image.freepik.com/free-vector/modern-pixel-concept-logo_1103-1042.jpg");
        put(5, "https://image.freepik.com/free-vector/dart-club-logo_1103-997.jpg");
        put(6, "https://image.freepik.com/free-vector/basketball-club-logo_1103-924.jpg");
        put(7, "https://image.freepik.com/free-vector/logo-with-a-rocket-and-gear_1103-644.jpg");
        put(8, "https://image.freepik.com/free-vector/poker-club-logo_1103-968.jpg");
        put(9, "https://image.freepik.com/free-vector/blue-tecgo_1103-822.jpg");
        put(10, "https://image.freepik.com/free-vector/infinity-star-logo_1103-982.jpg");
        put(11, "https://image.freepik.com/free-vector/royal-golfing-logo_1103-961.jpg");
    }};


    public static String generateBatchPayload(int products, int variants){
        //Load batch info
        collectionId = StringUtils.defaultIfEmpty(RequestFactory.collectionId, "XXXXXXXXXX");
        subscriptionId = StringUtils.defaultIfEmpty(RequestFactory.subsIdCreate, "ZZZZZZZZZZ");

        String requestBody;
        JSONObject fullBatch = new JSONObject();
        fullBatch.put("batchNumber", 0);
        fullBatch.put("totalBatches", 1);
        fullBatch.put("targetUrl", RequestFactory.integrationHost.concat(":").concat(String.valueOf(RequestFactory.integrationPort))
                .concat("/integrations/").concat(IntegrationTest.IntegrationId.toString()).concat("/items"));
        fullBatch.put("createdAt", Instant.now().toString());
        fullBatch.put("eventName", "product.created");
        fullBatch.put("organizationId", organizationId);
        fullBatch.put("subscriptionId", subscriptionId);
        fullBatch.put("collection", collectionBuilder());
        fullBatch.put("batch", batchBuilder(products, variants));

        requestBody = fullBatch.toJSONString();
        requestBody = requestBody.replaceAll("\"empty\"", "null");
        requestBody = requestBody.replaceAll("\\\\", "");

        /*
        System.out.println("\n===========PRINTING FULL BATCH GENERATED==========\n");
        System.out.println(requestBody);
        System.out.println("==================================================");
        */

        System.out.println("["+LocalDateTime.now()+"] ".concat("Batch successfully created."));
        return requestBody;
    }

    public static JSONArray batchBuilder(int products, int variants){
        if(products<=0){
            System.out.println("["+LocalDateTime.now()+"] ".concat("There should be at least one product in batch."));
            products=1;
        }
        if (variants > 0) {
            hasVariants=true;
            if(variants>10){
                System.out.println("["+LocalDateTime.now()+"] ".concat("Maximum number of Variants supported is 10"));
                variants=10;
            }
        }
        System.out.println("["+LocalDateTime.now()+"] ".concat("Processing ").concat(
                String.valueOf(products)).concat(" Product(s) with ").concat(String.valueOf(variants)).concat(" Variant(s)."));

        JSONArray batch = new JSONArray();
        for(int i=1; i<=products; i++){
            //Create Product
            batch.add(createProduct());
            //Create the Variants of the Product
            for(int j=1; j<=variants; j++){
                batch.add(createVariant(j));
            }
            //When variants, reach 100's when changing product
            if(hasVariants){
                while(!String.valueOf(sku).endsWith("00")){
                    sku++;
                }
                barcode=sku;
            }
            else {
                barcode++;
            }
        }
        return batch;
    }

    private static JSONObject collectionBuilder(){
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

        JSONObject collection = new JSONObject();
        collection.put("recordSrcAttributes", recordSrcAttributes);
        collection.put("imageColumns", imageColumns);
        collection.put("rowCount", 1);
        collection.put("created", Instant.now().toString());
        collection.put("recordSrcMapping", new JSONObject());
        collection.put("name", listName);
        collection.put("rowParentCount", 1);
        collection.put("lastRow", 1);
        collection.put("statusERD", "new");
        collection.put("lastUpdated", Instant.now().toString());
        collection.put("organizationId", organizationId);
        collection.put("map", empty);
        collection.put("id", collectionId);
        collection.put("type", "erd");

        return collection;
    }

    private static JSONObject createProduct(){
        JSONObject batchElement = new JSONObject();
        batchElement.put("sourceRecordId", sku);
        batchElement.put("created", Instant.now().toString());
        batchElement.put("_row", 0);
        batchElement.put("status", "Active");
        batchElement.put("lastUpdated", Instant.now().toString());

        JSONObject customFields = new JSONObject();
        customFields.put("body_html", "NOBODY");
        customFields.put("compare_at_price", 130);
        customFields.put("fulfillment_service", "manual");
        customFields.put("grams", 200);
        customFields.put("handle", "Manual");
        customFields.put("inventory_management", "Shopify");
        customFields.put("inventory_policy", "allow");
        customFields.put("inventory_quantity", 20);
        customFields.put("metafields_global_description_tag", "tags");
        customFields.put("metafields_global_title_tag", "General Item Main");
        customFields.put("option1_name", "Color");
        customFields.put("option2_name", "Size");
        customFields.put("option3_name", "Dimension");
        customFields.put("option1_value", "Main");
        customFields.put("option2_value", "M");
        customFields.put("option3_value", "33x22x22");
        customFields.put("position", position);
        customFields.put("price", 100);
        customFields.put("product_type", "General Item");
        customFields.put("published", true);
        customFields.put("published_scope", "global");
        customFields.put("requires_shipping", true);
        customFields.put("sku", sku);
        customFields.put("tags", "TestTags");
        customFields.put("taxable", true);
        customFields.put("template_suffix", "product.item.main");
        customFields.put("title", "Item".concat(String.valueOf(title)));
        customFields.put("vendor", "Nearsoft");
        customFields.put("weight", 4);
        customFields.put("weight_unit", "kg");
        customFields.put("alt_image_1", "Alt Img 1");
        customFields.put("alt_image_2", empty);
        customFields.put("alt_image_3", empty);
        customFields.put("alt_image_4", empty);
        customFields.put("alt_image_5", empty);
        customFields.put("alt_image_6", empty);
        customFields.put("alt_image_7", empty);
        customFields.put("alt_image_8", empty);
        customFields.put("alt_image_9", empty);
        customFields.put("alt_image_10", empty);
        customFields.put("alt_image_11", empty);
        customFields.put("alt_image_12", empty);
        customFields.put("alt_image_13", empty);
        customFields.put("alt_image_14", empty);
        customFields.put("alt_image_15", empty);
        customFields.put("alt_image_16", empty);
        customFields.put("alt_image_17", empty);
        customFields.put("alt_image_18", empty);
        customFields.put("alt_image_19", empty);
        customFields.put("alt_image_20", empty);
        customFields.put("alt_image_21", empty);
        customFields.put("alt_image_22", empty);
        customFields.put("alt_image_23", empty);
        customFields.put("alt_image_24", empty);
        customFields.put("alt_image_25", empty);
        customFields.put("alt_image_26", empty);
        customFields.put("alt_image_27", empty);
        customFields.put("alt_image_28", empty);
        customFields.put("alt_image_29", empty);
        customFields.put("alt_image_30", empty);

        if(!hasVariants){
            customFields.put("barcode", barcode);
            title++;
        }

        batchElement.put("customFields", customFields);

        JSONObject thumbnail = new JSONObject();
        String filename = imageURL.get(1).toString().replaceAll("https://image\\.freepik\\.com/free-vector/", "");
        thumbnail.put("url", imageURL.get(1));
        thumbnail.put("filename", filename);

        JSONObject imageElement = new JSONObject();
        imageElement.put("thumbnail", thumbnail);
        imageElement.put("id", createRandomID());
        imageElement.put("filename", filename);
        imageElement.put("position", 0);
        imageElement.put("url", imageURL.get(1));
        imageElement.put("fromUrl", true);

        JSONObject images = new JSONObject();
        images.put(filename, imageElement);
        batchElement.put("images", images);
        batchElement.put("id", createRandomID());
        batchElement.put("collectionId", collectionId);

        sku++;
        position++;

        return batchElement;
    }

    private static JSONObject createVariant(int variantNumber){
        JSONObject batchElement = new JSONObject();
        batchElement.put("sourceParentId", barcode);
        batchElement.put("sourceRecordId", sku);
        batchElement.put("created", Instant.now().toString());
        batchElement.put("_row", 0);
        batchElement.put("status", "Active");
        batchElement.put("lastUpdated", Instant.now().toString());

        JSONObject customFields = new JSONObject();
        customFields.put("barcode", barcode);
        customFields.put("body_html", "NOBODY");
        customFields.put("compare_at_price", 150);
        customFields.put("fulfillment_service", "manual");
        customFields.put("grams", 300);
        customFields.put("handle", "Manual");
        customFields.put("inventory_management", "Shopify");
        customFields.put("inventory_policy", "allow");
        customFields.put("inventory_quantity", 10);
        customFields.put("metafields_global_description_tag", "tags");
        customFields.put("metafields_global_title_tag", "General Item Other");
        customFields.put("option1_name", "Color");
        customFields.put("option2_name", "Size");
        customFields.put("option3_name", "Dimension");
        customFields.put("option1_value", "Other");
        customFields.put("option2_value", "L");
        customFields.put("option3_value", "45x34x34");
        customFields.put("position", position);
        customFields.put("price", 120);
        customFields.put("product_type", "General Item");
        customFields.put("published", true);
        customFields.put("published_scope", "global");
        customFields.put("requires_shipping", true);
        customFields.put("sku", sku);
        customFields.put("tags", "TestTags");
        customFields.put("taxable", true);
        customFields.put("template_suffix", "product.item.variant");
        customFields.put("title", "Item".concat(String.valueOf(title)).concat("-").concat(String.valueOf(variantNumber)));
        customFields.put("vendor", "Nearsoft");
        customFields.put("weight", 5);
        customFields.put("weight_unit", "kg");

        customFields.put("alt_image_1", "Alt Img 1");
        customFields.put("alt_image_2", empty);
        customFields.put("alt_image_3", empty);
        customFields.put("alt_image_4", empty);
        customFields.put("alt_image_5", empty);
        customFields.put("alt_image_6", empty);
        customFields.put("alt_image_7", empty);
        customFields.put("alt_image_8", empty);
        customFields.put("alt_image_9", empty);
        customFields.put("alt_image_10", empty);
        customFields.put("alt_image_11", empty);
        customFields.put("alt_image_12", empty);
        customFields.put("alt_image_13", empty);
        customFields.put("alt_image_14", empty);
        customFields.put("alt_image_15", empty);
        customFields.put("alt_image_16", empty);
        customFields.put("alt_image_17", empty);
        customFields.put("alt_image_18", empty);
        customFields.put("alt_image_19", empty);
        customFields.put("alt_image_20", empty);
        customFields.put("alt_image_21", empty);
        customFields.put("alt_image_22", empty);
        customFields.put("alt_image_23", empty);
        customFields.put("alt_image_24", empty);
        customFields.put("alt_image_25", empty);
        customFields.put("alt_image_26", empty);
        customFields.put("alt_image_27", empty);
        customFields.put("alt_image_28", empty);
        customFields.put("alt_image_29", empty);
        customFields.put("alt_image_30", empty);

        batchElement.put("customFields", customFields);

        JSONObject thumbnail = new JSONObject();
        String filename = imageURL.get(variantNumber+1).toString().replaceAll("https://image\\.freepik\\.com/free-vector/", "");
        thumbnail.put("url", imageURL.get(variantNumber+1));
        thumbnail.put("filename", filename);

        JSONObject imageElement = new JSONObject();
        imageElement.put("thumbnail", thumbnail);
        imageElement.put("id", createRandomID());
        imageElement.put("filename", filename);
        imageElement.put("position", 0);
        imageElement.put("url", imageURL.get(variantNumber+1));
        imageElement.put("fromUrl", true);

        JSONObject images = new JSONObject();
        images.put(filename, imageElement);
        batchElement.put("images", images);

        sku++;
        title++;
        position++;

        batchElement.put("id", createRandomID());
        batchElement.put("collectionId", collectionId);

        return batchElement;

    }

    private static String createRandomID(){
        return RandomStringUtils.randomAlphanumeric(10).toUpperCase();
    }
}
