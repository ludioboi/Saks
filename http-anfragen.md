okhttp-library: http-Anfragen an die Datenbank

Asynchron GET:
@Test
public void whenAsynchronousGetRequest_thenCorrect() {
    Request request = new Request.Builder()
      .url(BASE_URL + "/date")
      .build();

    Call call = client.newCall(request);
    call.enqueue(new Callback() {
        public void onResponse(Call call, Response response) 
          throws IOException {
            // ...
        }
        
        public void onFailure(Call call, IOException e) {
            fail();
        }
    });
}

Query-GET:
@Test
public void whenGetRequestWithQueryParameter_thenCorrect() 
  throws IOException {
    
    HttpUrl.Builder urlBuilder 
      = HttpUrl.parse(BASE_URL + "/ex/bars").newBuilder();
    urlBuilder.addQueryParameter("id", "1");

    String url = urlBuilder.build().toString();

    Request request = new Request.Builder()
      .url(url)
      .build();
    Call call = client.newCall(request);
    Response response = call.execute();

    assertThat(response.code(), equalTo(200));
}

POST-Request:
@Test
public void whenSendPostRequest_thenCorrect() 
  throws IOException {
    RequestBody formBody = new FormBody.Builder()
      .add("username", "test")
      .add("password", "test")
      .build();

    Request request = new Request.Builder()
      .url(BASE_URL + "/users")
      .post(formBody)
      .build();

    Call call = client.newCall(request);
    Response response = call.execute();
    
    assertThat(response.code(), equalTo(200));
}
