import java.util.Arrays;
import java.util.List;

/**
 * Test runner
 */
public class test {

    private static int passed = 0;
    private static int failed = 0;

    /** helper กลาง — พิมพ์ PASS/FAIL และนับผลให้เอง */
    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    public static void main(String[] args) {
        boolean assertsOn = false;
        assert assertsOn = true;
        if (!assertsOn) {
            System.out.println("WARNING: assertions disabled"
                    + " - re-run with: java -ea test\n");
        }

        System.out.println("=== Todolist Test Suite ===\n");

        testCreators();
        testAdd();
        testRemove();
        testObservers();
        testProducer();
        testExposure();

        System.out.println("\n=== Summary ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total : " + (passed + failed));
        System.out.println(failed == 0 ? "ALL TESTS PASSED" : "SOME TESTS FAILED");

        if (failed > 0) {
            System.exit(1);
        }
    }

    // Creator เทส constructor ทั้ง 2 ตัว ทั้งแบบที่ให้ผ่านและแบบที่ต้อง error
    private static void testCreators() {
        System.out.println("-- Creators --");

        check("new() -> empty", new BoundedStack().getAllTasks().isEmpty());

        // boundary ลองขอบสุด ๆ ดู initial = 0 กับ initial = MAX_TASKS ต้องไม่ error
        check("new(0) -> empty", new BoundedStack(0).getAllTasks().isEmpty());
        boolean threwAtUpperBound = false;
        try {
            new BoundedStack(BoundedStack.MAX_TASKS);
        } catch (IllegalArgumentException e) {
            threwAtUpperBound = true;
        }
        check("new(MAX_TASKS) -> does not throw", !threwAtUpperBound);

        // partition: input ผิดเงื่อนไข ใส่ค่าผิด ๆ ไป มันต้อง throw exception ออกมา
        boolean threwNegative = false;
        try {
            new BoundedStack(-1);
        } catch (IllegalArgumentException e) {
            threwNegative = true;
        }
        check("new(-1) -> throws IllegalArgumentException", threwNegative);

        boolean threwOverMax = false;
        try {
            new BoundedStack(BoundedStack.MAX_TASKS + 1);
        } catch (IllegalArgumentException e) {
            threwOverMax = true;
        }
        check("new(MAX_TASKS + 1) -> throws IllegalArgumentException", threwOverMax);
    }

    // Mutator เทส addTasks() ว่าเก็บลำดับถูกไหม แล้วกันข้อความแปลก ๆ ได้จริงไหม
    private static void testAdd() {
        System.out.println("\n-- Add --");

        BoundedStack s = new BoundedStack();
        check("add(ทำการบ้าน) -> returns true", s.addTask("ทำการบ้าน"));
        check("add(ทำการบ้าน) -> found by contains", s.contains("ทำการบ้าน"));

        s.addTask("อ่านหนังสือ");
        s.addTask("ซื้อของ");
        check("add preserves insertion order",
                s.getAllTasks().equals(Arrays.asList("ทำการบ้าน","อ่านหนังสือ","ซื้อของ")));

        // partition: เพิ่ม Task ซ้ำได้
        s.addTask("อ่านหนังสือ"); 
        check("duplicate tasks are allowed", s.getAllTasks().size() == 4);

        // partition: input ผิดเงื่อนไข ลองใส่ค่าผิด ๆ แบบต่าง ๆ ดูว่า error ถูกไหม
        boolean threwNull = false;
        try {
            s.addTask(null);
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("add(null) -> throws IllegalArgumentException", threwNull);

        boolean threwEmpty = false;
        try {
            s.addTask("");
        } catch (IllegalArgumentException e) {
            threwEmpty = true;
        }
        check("add(empty string) -> throws IllegalArgumentException", threwEmpty);

        boolean threwBlank = false; 
        try { 
            s.addTask(" "); } 
        catch (IllegalArgumentException e) {
            threwBlank = true; 
        } check("add(blank string) -> throws IllegalArgumentException", threwBlank);

        
    }

    // Mutator เทส remove() ทั้งกรณีลบได้จริงกับกรณีตำแหน่งไม่มี
    private static void testRemove() {
        System.out.println("\n-- Remove --");

        BoundedStack s = new BoundedStack();
        s.addTask("ทำการบ้าน"); 
        s.addTask("อ่านหนังสือ"); 
        s.addTask("ซื้อของ");

        check("remove(1) -> returns true", s.removeTask(1));
        check("remove keeps the others in order",
                s.getAllTasks().equals(Arrays.asList("ทำการบ้าน", "ซื้อของ")));

        // boundary ลบรายการที่ไม่มีอยู่ (เกินขอบบน กับ ติดลบ) ไม่ error แค่คืน false
        check("remove(index เกินขอบบน) -> returns false", !s.removeTask(99));
        check("remove(index ติดลบ) -> returns false", !s.removeTask(-1));

        // boundary ลบไปเรื่อย ๆ จนหมด list ต้องว่างจริง
        s.removeTask(0);
        s.removeTask(0);
        check("remove all -> empty", s.getAllTasks().isEmpty());
    }

    // Observer เทสพวก getTask, contains, getAllTasks ว่าดูค่าอย่างเดียว ไม่ไปแก้ข้อมูล (no side effect)
    private static void testObservers() {
        System.out.println("\n-- Observers --");

        BoundedStack s = new BoundedStack();
        s.addTask("ทำการบ้าน"); 
        s.addTask("อ่านหนังสือ");

        check("getTask(0)/getTask(1) return tasks in order",
        s.getTask(0).equals("ทำการบ้าน") &&
        s.getTask(1).equals("อ่านหนังสือ"));
        check("contains finds an existing task",s.contains("ทำการบ้าน"));
        check("contains rejects a missing task",!s.contains("ไปเที่ยว"));

        boolean threwOutOfBounds = false;
        try {
            s.getTask(99);
        } catch (IndexOutOfBoundsException e) {
            threwOutOfBounds = true;
        }
        check("get(index นอกขอบเขต) -> throws IndexOutOfBoundsException", threwOutOfBounds);

        int before = s.getAllTasks().size();
        s.getAllTasks();
        s.contains("ทำการบ้าน");
        check("observers have no side effects", s.getAllTasks().size() == before);
    }

    // Producer เทส sortAlphabetically() ว่าคืนตัวใหม่จริง ไม่แก้ตัวเดิม
    private static void testProducer() {
        System.out.println("\n-- Producer (sortAlphabetically) --");
        BoundedStack original = new BoundedStack();
        original.addTask("homework"); 
        original.addTask("book"); 
        original.addTask("shopping");

        BoundedStack sorted = original.sortAlphabetically();
        check("sortAlphabetically orders tasks alphabetically",
            sorted.getAllTasks().equals( Arrays.asList("book", "homework", "shopping") ));
        check("sortAlphabetically does not mutate the original", 
        original.getAllTasks().equals( Arrays.asList("homework", "book", "shopping") ));

        // boundary ลอง sort ตอน list ว่าง ๆ ดู ต้องไม่พัง
        check("sorting an empty todo list is safe",
                new BoundedStack().sortAlphabetically().getAllTasks().isEmpty());
    }

    //  เช็คว่า getAllTasks() คืนสำเนามาจริง ๆ ไม่ใช่ list ตัวจริงข้างในของเรา
    private static void testExposure() {
        System.out.println("\n-- Representation Exposure --");

        BoundedStack s = new BoundedStack();
        s.addTask("ทำการบ้าน");

        List<String> got = s.getAllTasks();
        got.add("Hack");
        check("mutating the result of getAllTasks() does not affect the todo list", 
            s.getAllTasks().size() == 1 && !s.contains("Hack"));

        check("getAllTasks() returns a fresh list each call", s.getAllTasks() != s.getAllTasks());
    }
}