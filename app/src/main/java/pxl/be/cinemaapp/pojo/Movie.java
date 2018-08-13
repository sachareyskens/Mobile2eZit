package pxl.be.cinemaapp.pojo;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
public class Movie implements Parcelable{
    private Integer voteCount;
    private Integer id;
    private Double voteAverage;
    private String title;
    private Double popularity;
    private Bitmap poster;
    private String originalLanguage;
    private String originalTitle;
    private List<Genre> genres;
    private Bitmap backdrop;
    private Boolean adult;
    private String overview;
    private String releaseDate;
    private String videoUrl;
    private boolean isCached;

    public Movie() {
        isCached = false;
    }

    public Movie(Integer voteCount, Integer id, Double voteAverage, String title, Double popularity, Bitmap poster, String originalLanguage, String originalTitle, List<Genre> genres, Bitmap backdrop, Boolean adult, String overview, String releaseDate, String videoUrl) {
        this.voteCount = voteCount;
        this.id = id;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.poster = poster;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genres = genres;
        this.backdrop = backdrop;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.videoUrl = videoUrl;
    }

    public Movie(Result movie) {
    }

    public String getGenresString(){
        String genresStr = "";

        for (Genre g : genres) {
            genresStr += g.getName() + ", ";
        }
        if (!genresStr.equals("")) {
            genresStr = genresStr.substring(0, genresStr.length() - 2);
        }
        return genresStr;
    }

    public static byte[] convertBitmapByteArray(Bitmap bitmap){
        int size = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();
    }


    protected Movie(Parcel in) {
        voteCount = in.readByte() == 0x00 ? null : in.readInt();
        id = in.readByte() == 0x00 ? null : in.readInt();
        voteAverage = in.readByte() == 0x00 ? null : in.readDouble();
        title = in.readString();
        popularity = in.readByte() == 0x00 ? null : in.readDouble();
        poster = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        originalLanguage = in.readString();
        originalTitle = in.readString();
        if (in.readByte() == 0x01) {
            genres = new ArrayList<Genre>();
            in.readList(genres, Genre.class.getClassLoader());
        } else {
            genres = null;
        }
        backdrop = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        byte adultVal = in.readByte();
        adult = adultVal == 0x02 ? null : adultVal != 0x00;
        overview = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (voteCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(voteCount);
        }
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (voteAverage == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(voteAverage);
        }
        dest.writeString(title);
        if (popularity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(popularity);
        }
        dest.writeValue(poster);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        if (genres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genres);
        }
        dest.writeValue(backdrop);
        if (adult == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (adult ? 0x01 : 0x00));
        }
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Bitmap getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(Bitmap backdrop) {
        this.backdrop = backdrop;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public boolean isCached() {
        return isCached;
    }

    public void setCached(boolean cached) {
        isCached = cached;
    }



}

