package graphql.mapper.request;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		RequestClientFactory clientFactory = new RequestClientFactory();
		try {
			clientFactory.sendGetRequest("https://adamis.com.br/mockiot/wifilist.php", null, null, null, false,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
