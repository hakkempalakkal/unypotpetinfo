package com.samyotech.petstand.models;

import android.os.Parcel;
import androidx.annotation.NonNull;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

public class DemoItem implements AsymmetricItem {

  private int columnSpan;
  private int rowSpan;
  private int position;

  String description;
  String gallery_id;
  String image_path;
  String title;
  String user_profile_pic;

  public DemoItem() {
    this(0, 0, 0);
  }

  public DemoItem(int columnSpan, int rowSpan, int position) {
    this.columnSpan = columnSpan;
    this.rowSpan = rowSpan;
    this.position = position;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getGallery_id() {
    return gallery_id;
  }

  public void setGallery_id(String gallery_id) {
    this.gallery_id = gallery_id;
  }

  public String getImage_path() {
    return image_path;
  }

  public void setImage_path(String image_path) {
    this.image_path = image_path;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUser_profile_pic() {
    return user_profile_pic;
  }

  public void setUser_profile_pic(String user_profile_pic) {
    this.user_profile_pic = user_profile_pic;
  }

  public DemoItem(Parcel in) {
    readFromParcel(in);
  }

  @Override public int getColumnSpan() {
    return columnSpan;
  }

  @Override public int getRowSpan() {
    return rowSpan;
  }

  public int getPosition() {
    return position;
  }

  @Override public String toString() {
    return String.format("%s: %sx%s", position, rowSpan, columnSpan);
  }

  @Override public int describeContents() {
    return 0;
  }

  private void readFromParcel(Parcel in) {
    columnSpan = in.readInt();
    rowSpan = in.readInt();
    position = in.readInt();
  }

  @Override public void writeToParcel(@NonNull Parcel dest, int flags) {
    dest.writeInt(columnSpan);
    dest.writeInt(rowSpan);
    dest.writeInt(position);
  }

  /* Parcelable interface implementation */
  public static final Creator<DemoItem> CREATOR = new Creator<DemoItem>() {

    @Override public DemoItem createFromParcel(@NonNull Parcel in) {
      return new DemoItem(in);
    }

    @Override @NonNull
    public DemoItem[] newArray(int size) {
      return new DemoItem[size];
    }
  };
}
