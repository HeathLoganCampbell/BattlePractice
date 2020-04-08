package com.battlechunk.practice.match.matchtypes;

import com.battlechunk.practice.match.Match;

public class BuildUHC extends Match
{
    public BuildUHC(int id)
    {
        super(id,"BuildUHC");

        this.blockBreak = true;
        this.blockPlace = true;
    }
}
