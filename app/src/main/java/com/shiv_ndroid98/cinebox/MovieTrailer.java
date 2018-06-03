
package com.shiv_ndroid98.cinebox;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieTrailer {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<VideoResults> vresults = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<VideoResults> getResults() {
        return vresults;
    }

    public void setResults(List<VideoResults> results) {
        this.vresults = results;
    }

}
