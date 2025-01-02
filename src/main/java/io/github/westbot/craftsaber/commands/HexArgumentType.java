package io.github.westbot.craftsaber.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

public class HexArgumentType implements ArgumentType<Integer> {

    public static final DynamicCommandExceptionType INVALID_INT = new DynamicCommandExceptionType(o -> Text.literal("Invalid int: " + o));

    public static final Collection<String> EXAMPLES = List.of(
        "000000",
        "FF0000",
        "00FF00",
        "0000FF",
        "FFFFFF"
    );

    public static int getHex(final CommandContext<?> context, final String name) {
        return context.getArgument(name, int.class);
    }
    
    private static final Collection<Character> VALID = List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F');

    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {

        int beginning = reader.getCursor();
        
        if (!reader.canRead()) {
            reader.skip();
        }
        
        while (reader.canRead() && (VALID.contains(reader.peek()))) {
            reader.skip();
        }

        String iStr = reader.getString().substring(beginning, reader.getCursor());

        try {
            return Integer.parseInt(iStr, 16);
        } catch (NumberFormatException e) {
            throw INVALID_INT.createWithContext(reader, e.getMessage());
        }
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
