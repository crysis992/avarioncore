/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.TestConv can not be copied and/or distributed without the express
 *  permission of crysis992
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of AvarionCraft.de and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AvarionCraft.de
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AvarionCraft.de.
 *
 */

package net.crytec.commands.administration;

import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class TestConv extends StringPrompt {

	@Override
	public String getPromptText(ConversationContext context) {
		return "Bitte gebe den neuen Namen in den Chat ein:";
	}

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		
		if (input.equals("end")) {
			context.getForWhom().sendRawMessage("§6> §eEingabe beendet!");
			return Prompt.END_OF_CONVERSATION;
		}
		
		
		context.setSessionData("text", input);
		
		return new BooleanPrompt() {
			
			@Override
			public String getPromptText(ConversationContext context) {
				return "True oder False?!";
			}
			
			@Override
			protected Prompt acceptValidatedInput(ConversationContext context, boolean input) {
				context.getForWhom().sendRawMessage("Dein Input war: " + input + " : " + context.getAllSessionData().get("text"));
				return Prompt.END_OF_CONVERSATION;
			}
		};
		
		
	}
}
