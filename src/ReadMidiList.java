import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// got this here! https://stackoverflow.com/questions/3850688/reading-midi-files-in-java


public class ReadMidiList {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public static final int QUARTER_TICS = 960;

    public static List<Note> ParseMidiFile(String midifileName) throws InvalidMidiDataException, IOException {
        List<Note> noteList = new ArrayList<>();
        Sequence sequence = MidiSystem.getSequence(new File(midifileName));

        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");



                    if (sm.getCommand() == NOTE_ON || sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();

                        if(velocity > 0 && sm.getCommand() == NOTE_ON) {
                            System.out.println("note " + key + " velocity: " + velocity);
                            noteList.add(new Note(sm.getChannel(), key,velocity,event.getTick(),-1));
                        } else {
                            // set note ending tic
                            for (Note thisNote: noteList) {
                                if(thisNote.getPitch() == key) {
                                    if(thisNote.getDurationTics() == -1) {
                                        thisNote.setDurationTics(event.getTick() - thisNote.getStartTic());
                                    }
                                }
                            }
                            System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                        }
                    } else {
                        System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    System.out.println("Other message: " + message.getClass());
                }
            }
            System.out.println();
        }
        return noteList;
    }
    public static void WriteTextFile(List<Note> noteList, String outputFilename) {
        try {
            FileWriter myWriter = new FileWriter(outputFilename);
            myWriter.write("type channel pitchMIDI velocityMIDI startTic durationTic");
            for (Note thisNote: noteList
            ) {
                String toWrite = "note " + thisNote.getChannel() + " " + thisNote.getPitch() + " " + thisNote.getVelocity() + " " + thisNote.getStartTic() + " " + thisNote.getDurationTics() + "\n";
                myWriter.write(toWrite);
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

            System.out.println();

    }
    public static void main(String[] args) throws Exception {
        List<Note> noteList = ParseMidiFile("background3.mid");
        WriteTextFile(noteList, "output2.txt");

    }
}


/*
https://stackoverflow.com/questions/3850688/reading-midi-files-in-java
public class Test {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public static void main(String[] args) throws Exception {
        Sequence sequence = MidiSystem.getSequence(new File("test.mid"));

        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else {
                        System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    System.out.println("Other message: " + message.getClass());
                }
            }

            System.out.println();
        }

    }
}
 */