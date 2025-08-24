# Level Gap Resistance System

## 📊 **Damage Multipliers & Resistance Numbers:**

### **When YOU Attack Lower Level Enemies:**
| Level Gap | Damage Multiplier | Example Damage |
|-----------|------------------|----------------|
| **10-14 levels** | **1.4x** | 300 → **420 damage** |
| **15-19 levels** | **1.7x** | 300 → **510 damage** |
| **20+ levels** | **2.0x** | 300 → **600 damage** |

### **When Lower Level Enemies Attack YOU:**
| Your Level Advantage | Damage Resistance | Incoming Damage Reduced |
|---------------------|-------------------|------------------------|
| **13-14 levels higher** | **45% reduction** | 200 → **110 damage** |
| **15-19 levels higher** | **60% reduction** | 200 → **80 damage** |
| **20+ levels higher** | **75% reduction** | 200 → **50 damage** |

---

## 🎯 **Current Setup (Level 30 vs Level 15):**

### **When Level 30 You Attack Level 15 Enemy:**
- **Level Gap**: 15 levels
- **Damage Multiplier**: **1.7x**
- **Example**: 300 raw → **510 boosted** → ~400-450 final (after defenses)
- **Expected HP Damage**: **~70-75%**

### **When Level 15 Enemy Attacks Level 30 You:**
- **Your Level Advantage**: 15 levels  
- **Damage Resistance**: **60% reduction**
- **Example**: 200 raw → **80 resisted** → ~60-70 final (after defenses)
- **Expected HP Damage**: **~3-5%** (almost no damage!)

---

## 💪 **Resistance Breakdown:**

### **13+ Level Advantage: 45% Damage Reduction**
- **Incoming 100 damage** → **55 damage taken**
- **Moderate protection** - you're clearly stronger

### **15+ Level Advantage: 60% Damage Reduction**  
- **Incoming 100 damage** → **40 damage taken**
- **Strong protection** - low levels barely scratch you

### **20+ Level Advantage: 75% Damage Reduction**
- **Incoming 100 damage** → **25 damage taken**  
- **Extreme protection** - almost immunity to weak enemies

---

## 🔥 **Expected Console Log Example:**

```
============================================================
AI TURN: Ahri uses Orb of Deception  
============================================================
DAMAGE CALC: 60 base + 0 AD scaling + 137 AP scaling = 197 raw
LEVEL GAP RESIST: 197 damage × 40% (Lv15 defender advantage) = 79 resisted
DEFENSE: 44 MR - 20 Pen = 24 effective (19.4% reduction)
FINAL: 79 → 64 final
IMPACT: 3.9% of 1625 HP | 25.4 hits to kill
============================================================
```

**Perfect! Now you should feel appropriately tanky against lower level opponents while still dealing devastating damage to them!** 🛡️⚡