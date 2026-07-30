import java.util.ArrayList;
import java.util.List;

/**
 * ToDoList - ADT แทนรายการสิ่งที่ต้องทำ
 * form ราชพฤกษ์ อินทพันธุ์ และ ณัชชารินทร์ นามราษฎร์
 *
 * ลำดับของรายการสิ่งที่ต้องทำ
 */
public class BoundedStack {

    private final List<String> tasks;
    public static final int MAX_TASKS = 100;

    // ===== Representation =====

    // AF(tasks) = ลำดับของรายการสิ่งที่ต้องทำทั้งหมด

    // Representation Invariant (RI):
    // - tasks ต้องไม่เป็น null
    // - จำนวนรายการต้องไม่เกิน MAX_TASKS
    // - รายการแต่ละรายการต้องไม่เป็น null
    // - รายการแต่ละรายการต้องไม่เป็นสตริงว่าง
    // - รายการแต่ละรายการต้องไม่เป็นช่องว่างล้วน

    private void checkRep() {
        assert tasks != null : "tasks ต้องไม่เป็น null";
        assert tasks.size() <= MAX_TASKS : "จำนวน task ต้องไม่เกิน MAX_TASKS";
        for (String task : tasks) {
            assert task != null : "task ต้องไม่เป็น null";
            assert !task.isEmpty() : "task ต้องไม่เป็นสตริงว่าง";
            assert !task.trim().isEmpty() : "task ต้องไม่เป็นช่องว่างล้วน";
        }
    }

    // ===== Creator =====

    /**
     * สร้าง ToDoList ว่าง
     */
    public BoundedStack() {
        this.tasks = new ArrayList<>(MAX_TASKS);
        checkRep();
    }

    /**
     * สร้าง ToDoList พร้อมกำหนดความจุเริ่มต้น
     *
     * @param initial ความจุเริ่มต้น
     * @throws IllegalArgumentException ถ้า initial ไม่ถูกต้อง
     */
    public BoundedStack(int initial) {
        if (initial < 0 || initial > MAX_TASKS) throw new IllegalArgumentException("initial ต้องอยู่ระหว่าง 0 ถึง " + MAX_TASKS);
        this.tasks = new ArrayList<>(initial);
        checkRep();
    }

    // ===== Mutators =====

    /**
     * เพิ่มรายการสิ่งที่ต้องทำ
     *
     * @param task รายการที่ต้องการเพิ่ม
     * @return true ถ้าเพิ่มสำเร็จ,
     *         false ถ้ารายการเต็ม
     * @throws IllegalArgumentException ถ้า task ไม่ถูกต้อง
     */
    public boolean addTask(String task) {
        if (task == null || task.trim().isEmpty()) throw new IllegalArgumentException("task ห้ามเป็นค่าว่าง");
        if (tasks.size() >= MAX_TASKS) return false;
        tasks.add(task);
        checkRep();
        return true;
    }

    /**
     * ลบรายการตามตำแหน่ง
     *
     * @param index ตำแหน่งของรายการ
     * @return true ถ้าลบสำเร็จ,
     *         false ถ้าตำแหน่งไม่ถูกต้อง
     */
    public boolean removeTask(int index) {
        if (index < 0 || index >= tasks.size())return false;
        tasks.remove(index);
        checkRep();
        return true;
    }

    /**
     * แก้ไขรายการ
     *
     * @param index ตำแหน่งของรายการ
     * @param newTask ข้อความใหม่
     * @return true ถ้าแก้ไขสำเร็จ
     */
    public boolean updateTask(int index, String newTask) {
        if (index < 0 || index >= tasks.size()) {
            return false;
        }
        if (newTask == null || newTask.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "task ใหม่ไม่ถูกต้อง"
            );
        }
        tasks.set(index, newTask);
        checkRep();
        return true;
    }

    // ===== Observers =====

    /**
     * คืนค่ารายการตามตำแหน่ง
     *
     * @param index ตำแหน่งของรายการ
     * @return รายการที่ตำแหน่งนั้น
     */
    public String getTask(int index) {
        if (index < 0 || index >= tasks.size()) throw new IndexOutOfBoundsException("index ไม่ถูกต้อง");
        return tasks.get(index);
    }

    /**
     * ตรวจสอบว่ามีรายการนี้อยู่หรือไม่
     *
     * @param task รายการที่ต้องการค้นหา
     * @return true ถ้าพบ
     */
    public boolean contains(String task) {
        return tasks.contains(task);
    }

    /**
     * คืนจำนวนรายการทั้งหมด
     *
     * @return จำนวนรายการ
     */
    public int size() {
        return tasks.size();
    }

    /**
     * คืนสำเนาของรายการทั้งหมด
     *
     * @return รายการทั้งหมด
     */
    public List<String> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    // ===== Producer =====

    /**
     * คืน ToDoList ใหม่ที่เรียงรายการตามตัวอักษร
     *
     * @return ToDoList ที่เรียงแล้ว
     */
    public BoundedStack sortAlphabetically() {
        List<String> copy = new ArrayList<>(tasks);
        copy.sort((a,b) -> a.compareTo(b));
        BoundedStack result = new BoundedStack(copy.size());
        for (String task : copy) {
            result.addTask(task);
        }
        return result;
    }

}
