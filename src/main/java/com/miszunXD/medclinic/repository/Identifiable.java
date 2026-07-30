package com.miszunXD.medclinic.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Identifiable <ID>{
    ID getId();
}
