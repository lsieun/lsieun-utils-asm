package lsieun.asm.function.match;


@FunctionalInterface
public interface MemberMatch {
    boolean test(String currentOwner, int access, String name, String descriptor);

    enum Bool implements MemberMatch {
        TRUE {
            @Override
            public boolean test(String currentOwner, int access, String name, String descriptor) {
                return true;
            }
        },
        FALSE {
            @Override
            public boolean test(String currentOwner, int access, String name, String descriptor) {
                return false;
            }
        };
    }

//    class And implements MemberMatch {
//        private final MemberMatch[] matches;
//
//        private And(MemberMatch... matches) {
//            this.matches = matches;
//        }
//
//        @Override
//        public boolean test(String currentOwner, int access, String name, String descriptor) {
//            for (MemberMatch match : matches) {
//                if (!match.test(currentOwner, access, name, descriptor)) {
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        public static And of(MemberMatch... matches) {
//            return new And(matches);
//        }
//    }

//    class Or implements MemberMatch {
//        private final MemberMatch[] matches;
//
//        private Or(MemberMatch... matches) {
//            this.matches = matches;
//        }
//
//        @Override
//        public boolean test(String currentOwner, int access, String name, String descriptor) {
//            for (MemberMatch match : matches) {
//                if (match.test(currentOwner, access, name, descriptor)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        public static Or of(MemberMatch... matches) {
//            return new Or(matches);
//        }
//    }

    static MemberMatch byName(TextMatch textMatch) {
        return ((currentOwner, access, name, descriptor) -> textMatch.test(name));
    }
}
