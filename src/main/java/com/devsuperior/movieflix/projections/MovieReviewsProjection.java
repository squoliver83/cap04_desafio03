package com.devsuperior.movieflix.projections;

public interface MovieReviewsProjection extends IdProjection<Long> {

    String getText();

    String getName();

}
