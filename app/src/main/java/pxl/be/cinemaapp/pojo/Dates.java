package pxl.be.cinemaapp.pojo;
import java.io.Serializable;
import java.util.HashMap;
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
        "maximum",
        "minimum"
})
public class Dates implements Serializable {

    @JsonProperty("maximum")
    private String maximum;
    @JsonProperty("minimum")
    private String minimum;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 4517306451155178650L;

    /**
     * No args constructor for use in serialization
     */
    public Dates() {
    }

    /**
     * @param minimum
     * @param maximum
     */
    public Dates(String maximum, String minimum) {
        super();
        this.maximum = maximum;
        this.minimum = minimum;
    }

    @JsonProperty("maximum")
    public String getMaximum() {
        return maximum;
    }

    @JsonProperty("maximum")
    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    @JsonProperty("minimum")
    public String getMinimum() {
        return minimum;
    }

    @JsonProperty("minimum")
    public void setMinimum(String minimum) {
        this.minimum = minimum;
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
        return new ToStringBuilder(this).append("maximum", maximum).append("minimum", minimum).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(minimum).append(additionalProperties).append(maximum).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Dates)) {
            return false;
        }
        Dates rhs = ((Dates) other);
        return new EqualsBuilder().append(minimum, rhs.minimum).append(additionalProperties, rhs.additionalProperties).append(maximum, rhs.maximum).isEquals();
    }


}
