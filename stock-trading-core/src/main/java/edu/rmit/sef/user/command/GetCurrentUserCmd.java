package edu.rmit.sef.user.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.security.CommandAuthority;
import edu.rmit.sef.core.security.Authority;

@CommandAuthority(Authority.USER)
public class GetCurrentUserCmd extends Command<GetCurrentUserResp> {

}
