package pxl.be.cinemaapp.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

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
        "totalResults",
        "resultsPerPage"
})
public class PageInfo implements Serializable, Parcelable {

    @JsonProperty("totalResults")
    private Integer totalResults;
    @JsonProperty("resultsPerPage")
    private Integer resultsPerPage;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    public final static Parcelable.Creator<PageInfo> CREATOR = new Creator<PageInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PageInfo createFromParcel(Parcel in) {
            return new PageInfo(in);
        }

        public PageInfo[] newArray(int size) {
            return (new PageInfo[size]);
        }

    };
    private final static long serialVersionUID = -6382609891101214189L;

    protected PageInfo(Parcel in) {
        this.totalResults = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.resultsPerPage = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.additionalProperties = ((Map<String, Object>) in.readValue((Map.class.getClassLoader())));
    }

    public PageInfo() {
    }

    @JsonProperty("totalResults")
    public Integer getTotalResults() {
        return totalResults;
    }

    @JsonProperty("totalResults")
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    @JsonProperty("resultsPerPage")
    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    @JsonProperty("resultsPerPage")
    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
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
        return new ToStringBuilder(this).append("totalResults", totalResults).append("resultsPerPage", resultsPerPage).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(totalResults).append(additionalProperties).append(resultsPerPage).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PageInfo) == false) {
            return false;
        }
        PageInfo rhs = ((PageInfo) other);
        return new EqualsBuilder().append(totalResults, rhs.totalResults).append(additionalProperties, rhs.additionalProperties).append(resultsPerPage, rhs.resultsPerPage).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(totalResults);
        dest.writeValue(resultsPerPage);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}
