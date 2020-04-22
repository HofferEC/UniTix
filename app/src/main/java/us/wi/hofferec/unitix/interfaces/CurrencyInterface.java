package us.wi.hofferec.unitix.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CurrencyInterface {
    String BASE_URL = "http://data.fixer.io/api/";

    @GET("latest?access_key=bb749dcf00ccf96476a7396ed9617362&symbols=USD,GBP")
    Call<String> getCurrencyRatesFromEUR();
}
