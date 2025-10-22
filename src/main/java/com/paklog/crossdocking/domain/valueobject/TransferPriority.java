package com.paklog.crossdocking.domain.valueobject;

public enum TransferPriority {
    URGENT(1),
    HIGH(2),
    NORMAL(3),
    LOW(4);
    
    private final int level;
    
    TransferPriority(int level) {
        this.level = level;
    }
    
    public int getLevel() {
        return level;
    }
}
