package com.smoothstack.BatchMicroservice.processor;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class TransactionSkipPolicy implements SkipPolicy {
    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        if(t instanceof Exception){
            skipCount+=1;
            System.out.println("number of lines skipped => " +skipCount);
            return true;
        }
        return false;
    }
}
