# Current Status of Project
This page tracks project progress and next steps.

## Current Block
- The current fingerprinting library (homer-law) produces inconsistent hash counts for songs of similar length.
- Example: the full song used to generate the test snippet only produced ~60 hashes over 3+ minutes and consistently had the *fewest* matches. Another song had 5x more hashes.
- This leads to unreliable matching—songs with more hashes dominate the results, regardless of actual similarity.

### What I’ve Tried
- Switched to a statistical matching method (based on variance, not count), but the low hash count still undermines accuracy due to insufficient match data.

### Next Steps
- Replace the current library with one that generates more hashes:
    - Try [Dejavu](https://github.com/worldveil/dejavu) (Python) to output hashes.
    - Or clone [Panako](https://github.com/JorenSix/Panako/tree/master?tab=readme-ov-file#usage) and build a custom Java JAR to extract hash arrays.
- For Python:
    - Either call scripts directly or run Dejavu in a container with a custom API that returns JSON.
    - Watch out for large output sizes—unknown scale.
- Long-term goal: build a custom fingerprinting algorithm, but need a working baseline first for comparison.

## Added
**17th June**
- Statistical approach to query matching
- Endpoint and logs for debugging database and fingerprinting issues
- Extra docs

**23rd May**
- End to end working endpoints for ingesting audio files and query matching an audio snippet
- Test endpoints (edited after use) for querying the database for hashes to debug whether DB is up and running
- 3 services for finger-printing, database management and query matching
- Homer-law fingerprinting JAR used as library to extract hashes