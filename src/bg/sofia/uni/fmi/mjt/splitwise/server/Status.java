package bg.sofia.uni.fmi.mjt.splitwise.server;

import java.util.Set;

public record Status (Set<Debt> owes, Set<Debt> isOwed){};