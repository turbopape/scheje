(ns scheje.library)

(def
  root-env
  {
   'true true
   'false false
   'else true
   :keywords ['= 'cond 'lambda 'if 'else 'display 'cons 'car 'cdr 'null? 'atom? '+ '- 'eq? '< '<= '> '>= '/ '* 'false 'true 'else]
   :syntax ['{:name let, :literals (),
              :rules (((let () body ... ) ((lambda() body ...)))
                      ((let ((var expr) ...) body ...) ((lambda (var ...) body ...) expr ...))
                      ((let let-name ((var expr)...) body ... ) (letrec ((let-name (lambda (var ...) body ...)))
                                                                        (let-name expr ...))))}

            '{:name letrec, :literals (),
              :rules (((letrec bindings body) (let bindings body)))}
            
            '{:name let*, :literals (),
              :rules (((let* () body ...) (let () body ...))
                      ((let* (binding) body ...) (let (binding) body ...))
                      ((let* (binding bnext ...) body ...) (let (binding) (let* (bnext ...) body ...))))}

            '{:name letrec*, :literals (),
              :rules (((letrec* bindings body) (let* bindings body)))}
            
            '{:name and, :literals (),
              :rules (((and x) x) ((and) true)
                      ((and x y ...) (if x (and y ...) false)))}
            '{:name or, :literals (),
              :rules (((or) true)
                      ((or x) x)
                      ((or x y ...) (if x true (or y ...))))}]})
