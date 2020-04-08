package com.battlechunk.practice.playerdata;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class PlayerData
{
    public static final int NO_MATCH = -1;
    public static final int NO_TEAM = 0;

    // -1 means they aren't in a match
    private int matchId = NO_MATCH;
    // 0 = spectator, 1 = team one, 2 = team two
    private int teamId = NO_TEAM;

    private @NonNull UUID uniqueId;
}
