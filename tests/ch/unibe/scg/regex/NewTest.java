package ch.unibe.scg.regex;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ch.unibe.scg.regex.TNFAToTDFA.DFAState;
import ch.unibe.scg.regex.TransitionTriple.Priority;

@SuppressWarnings("javadoc")
public final class NewTest {
  TNFAToTDFA nfa2dfa;
  State s0, s1, s2;
  Tag t0;
  TNFA tnfa;

  @SuppressWarnings({"unchecked", "rawtypes"})
  TNFA makeTheNFA() {
    State.resetCount();

    s0 = State.get();
    s1 = State.get();
    s2 = State.get();

    final TNFA tnfa = mock(TNFA.class);

    t0 = mock(Tag.class);

    when(t0.toString()).thenReturn("t0");
    when(t0.getGroup()).thenReturn(1);

    when(tnfa.allInputRanges()).thenReturn(Arrays.asList(InputRange.make('a')));
    when(tnfa.getInitialState()).thenReturn(s0);
    when(tnfa.availableTransitionsFor(eq(s0), isNull(Character.class))).thenReturn(
        Arrays.asList(new TransitionTriple(s1, Priority.NORMAL, t0), new TransitionTriple(s0,
            Priority.NORMAL, Tag.NONE)));
    when(tnfa.availableTransitionsFor(eq(s0), eq('a'))).thenReturn(
        Arrays.asList(new TransitionTriple(s0, Priority.NORMAL, Tag.NONE)));
    when(tnfa.availableTransitionsFor(s1, 'a')).thenReturn(
        Arrays.asList(new TransitionTriple(s2, Priority.NORMAL, Tag.NONE), new TransitionTriple(s1,
            Priority.NORMAL, Tag.NONE)));
    when(tnfa.availableTransitionsFor(s2, 'a')).thenReturn(new ArrayList());
    when(tnfa.isAccepting(eq(s2))).thenReturn(true);
    when(tnfa.isAccepting(eq(s1))).thenReturn(false);
    when(tnfa.isAccepting(eq(s0))).thenReturn(false);
    when(tnfa.allTags()).thenReturn(Arrays.asList(t0));
    return tnfa;
  }

  @Before
  public void setUp() {
    tnfa = makeTheNFA();
    nfa2dfa = TNFAToTDFA.make(tnfa);
  }

  @Test
  public void testInitialState() {
    assertEquals("[t0]", tnfa.allTags().toString());
    final DFAState converted = nfa2dfa.makeStartState();
    assertEquals("q0->[-1, -2], q1->[0, -2]", converted.toString());
  }
}