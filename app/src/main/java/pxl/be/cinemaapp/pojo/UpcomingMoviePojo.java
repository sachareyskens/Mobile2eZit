package pxl.be.cinemaapp.pojo;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "results",
        "page",
        "total_results",
        "dates",
        "total_pages"
})
public class UpcomingMoviePojo implements Serializable {

    @JsonProperty("results")
    private List<Result> results = null;
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("total_results")
    private Integer totalResults;
    @JsonProperty("dates")
    private Dates dates;
    @JsonProperty("total_pages")
    private Integer totalPages;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -6007073281102095145L;

    /**
     * No args constructor for use in serialization
     */
    public UpcomingMoviePojo() {
    }

    /**
     * @param results
     * @param dates
     * @param totalResults
     * @param page
     * @param totalPages
     */
    public UpcomingMoviePojo(List<Result> results, Integer page, Integer totalResults, Dates dates, Integer totalPages) {
        super();
        this.results = results;
        this.page = page;
        this.totalResults = totalResults;
        this.dates = dates;
        this.totalPages = totalPages;
    }

    @JsonProperty("results")
    public List<Result> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<Result> results) {
        this.results = results;
    }

    @JsonProperty("page")
    public Integer getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(Integer page) {
        this.page = page;
    }

    @JsonProperty("total_results")
    public Integer getTotalResults() {
        return totalResults;
    }

    @JsonProperty("total_results")
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    @JsonProperty("dates")
    public Dates getDates() {
        return dates;
    }

    @JsonProperty("dates")
    public void setDates(Dates dates) {
        this.dates = dates;
    }

    @JsonProperty("total_pages")
    public Integer getTotalPages() {
        return totalPages;
    }

    @JsonProperty("total_pages")
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("results", results).append("page", page).append("totalResults", totalResults).append("dates", dates).append("totalPages", totalPages).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(results).append(dates).append(totalResults).append(page).append(additionalProperties).append(totalPages).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UpcomingMoviePojo)) {
            return false;
        }
        UpcomingMoviePojo rhs = ((UpcomingMoviePojo) other);
        return new EqualsBuilder().append(results, rhs.results).append(dates, rhs.dates).append(totalResults, rhs.totalResults).append(page, rhs.page).append(additionalProperties, rhs.additionalProperties).append(totalPages, rhs.totalPages).isEquals();
    }


}
