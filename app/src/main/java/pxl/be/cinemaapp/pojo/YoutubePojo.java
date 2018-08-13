package pxl.be.cinemaapp.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
        "kind",
        "etag",
        "nextPageToken",
        "regionCode",
        "pageInfo",
        "items"
})
public class YoutubePojo implements Serializable, Parcelable {

    @JsonProperty("kind")
    private String kind;
    @JsonProperty("etag")
    private String etag;
    @JsonProperty("nextPageToken")
    private String nextPageToken;
    @JsonProperty("regionCode")
    private String regionCode;
    @JsonProperty("pageInfo")
    private PageInfo pageInfo;
    @JsonProperty("items")
    private List<Item> items = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    public final static Parcelable.Creator<YoutubePojo> CREATOR = new Creator<YoutubePojo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public YoutubePojo createFromParcel(Parcel in) {
            return new YoutubePojo(in);
        }

        public YoutubePojo[] newArray(int size) {
            return (new YoutubePojo[size]);
        }

    };
    private final static long serialVersionUID = 182581658065882466L;

    protected YoutubePojo(Parcel in) {
        this.kind = ((String) in.readValue((String.class.getClassLoader())));
        this.etag = ((String) in.readValue((String.class.getClassLoader())));
        this.nextPageToken = ((String) in.readValue((String.class.getClassLoader())));
        this.regionCode = ((String) in.readValue((String.class.getClassLoader())));
        this.pageInfo = ((PageInfo) in.readValue((PageInfo.class.getClassLoader())));
        in.readList(this.items, (pxl.be.cinemaapp.pojo.Item.class.getClassLoader()));
        this.additionalProperties = ((Map<String, Object>) in.readValue((Map.class.getClassLoader())));
    }

    public YoutubePojo() {
    }

    @JsonProperty("kind")
    public String getKind() {
        return kind;
    }

    @JsonProperty("kind")
    public void setKind(String kind) {
        this.kind = kind;
    }

    @JsonProperty("etag")
    public String getEtag() {
        return etag;
    }

    @JsonProperty("etag")
    public void setEtag(String etag) {
        this.etag = etag;
    }

    @JsonProperty("nextPageToken")
    public String getNextPageToken() {
        return nextPageToken;
    }

    @JsonProperty("nextPageToken")
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    @JsonProperty("regionCode")
    public String getRegionCode() {
        return regionCode;
    }

    @JsonProperty("regionCode")
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    @JsonProperty("pageInfo")
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    @JsonProperty("pageInfo")
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<Item> items) {
        this.items = items;
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
        return new ToStringBuilder(this).append("kind", kind).append("etag", etag).append("nextPageToken", nextPageToken).append("regionCode", regionCode).append("pageInfo", pageInfo).append("items", items).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(regionCode).append(etag).append(items).append(additionalProperties).append(pageInfo).append(nextPageToken).append(kind).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof YoutubePojo) == false) {
            return false;
        }
        YoutubePojo rhs = ((YoutubePojo) other);
        return new EqualsBuilder().append(regionCode, rhs.regionCode).append(etag, rhs.etag).append(items, rhs.items).append(additionalProperties, rhs.additionalProperties).append(pageInfo, rhs.pageInfo).append(nextPageToken, rhs.nextPageToken).append(kind, rhs.kind).isEquals();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(kind);
        dest.writeValue(etag);
        dest.writeValue(nextPageToken);
        dest.writeValue(regionCode);
        dest.writeValue(pageInfo);
        dest.writeList(items);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}

