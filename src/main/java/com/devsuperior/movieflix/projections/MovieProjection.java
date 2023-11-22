package com.devsuperior.movieflix.projections;

public interface MovieProjection extends IdProjection<Long> {

    String getTitle();
    String getSubTitle();
    Integer getYear();
    String getImgUrl();

}
