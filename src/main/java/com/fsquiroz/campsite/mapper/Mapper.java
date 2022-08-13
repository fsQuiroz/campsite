package com.fsquiroz.campsite.mapper;

public abstract class Mapper<SRC, DST> {

    public abstract DST map(SRC src);
}
