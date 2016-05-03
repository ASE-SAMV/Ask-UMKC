/**
 * Created by sravan on 4/6/2016.
 */
package com.aseproject.askumkc;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kiran on 3/18/2016.
 */

public class QueryBuilder {


    /**
     * Specify your database name here
     * @return
     */
    public String getDatabaseName() {

        //return "user";
        return "firstdetails";
    }

    /**
     * Specify your MongoLab API here
     * @return
     */
    public String getApiKey()
    {
        //return "3xzlRS5DzNYSRv9h5bfjnZzCyD8q98Z9";

        return "7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic";
    }

    /**
     * This constructs the URL that allows you to manage your database,
     * collections and documents
     * @return
     */
    public String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }

    /**
     * Completes the formating of your URL and adds your API key at the end
     * @return
     */
    public String docApiKeyUrl()
    {
        return "?apiKey="+getApiKey();
    }

    /**
     * Returns the docs101 collection
     * @return
     */

    public String docApiKeyUrl(String docid)
    {
        return "/"+docid+"?apiKey="+getApiKey();
    }

    public String documentRequest()
    {
        return "register";
    }

    /**
     * Builds a complete URL using the methods specified above
     * @return
     */
    public String buildContactsSaveURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }

    public String buildContactsGetURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }

    /**
     * Get a Mongodb document that corresponds to the given object id
     * @param doc_id
     * @return
     */
    public String buildContactsSaveURL(String doc_id)
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl(doc_id);
    }


    public String createContact(JSONObject j) throws JSONException {
//        return String
//                .format("{\"document\"  : {\"Product\": \"%s\", "
//                                + "\"Quantity\": \"%s\", \"Description\": \"%s\"}, \"safe\" : true}",
//                        contact.product, contact.quantity, contact.description);

        return String
                .format("{\"fname\": \"%s\", "
                                + "\"lastname\": \"%s\", \"number\": \"%s\"}",
                        j.getString("fname"),j.getString("lastname"), j.getString("number"));

       /* return String
                .format("{\"document\" :{\"fname\": \"%s\", "
                                + "\"lastname\": \"%s\", \"number\": \"%s\"}",
                        j.getString("fname"),j.getString("lastname"), j.getString("number"));*/
    }
}