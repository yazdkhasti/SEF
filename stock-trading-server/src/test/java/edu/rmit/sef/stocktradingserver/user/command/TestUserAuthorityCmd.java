package edu.rmit.sef.stocktradingserver.user.command;

import edu.rmit.command.core.Command;
import edu.rmit.command.core.NullResp;
import edu.rmit.command.security.CommandAuthority;
import edu.rmit.sef.core.security.Authority;

@CommandAuthority(Authority.USER)
public class TestUserAuthorityCmd extends Command<NullResp> {
}
