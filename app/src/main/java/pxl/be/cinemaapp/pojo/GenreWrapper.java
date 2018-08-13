package pxl.be.cinemaapp.pojo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
public class GenreWrapper implements Serializable {

    @JsonProperty("genres")
    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }
}

