package us.wi.hofferec.unitix.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CurrencyInterface {
    String BASE_URL = "https://api.exchangeratesapi.io/";

    @GET("latest?symbols=USD,GBP")
    Call<String> getCurrencyRatesFromEUR();
}
