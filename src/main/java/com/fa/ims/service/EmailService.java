package com.fa.ims.service;

import com.fa.ims.dto.EmailDto;
import com.fa.ims.dto.EmailSendUserDto;

import java.io.IOException;

import java.io.IOException;

public interface EmailService {
    boolean sendEmail(EmailDto emailDto) throws IOException;
}