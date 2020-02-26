package edu.com.deepdive.ontrack.service;

public interface OntrackService {

  static OntrackService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final OntrackService INSTANCE = getInstance();

  }
}
